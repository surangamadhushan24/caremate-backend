package com.nibm.echannelling.echannelingapplication.service;

import com.nibm.echannelling.echannelingapplication.dto.ChatRequest;
import com.nibm.echannelling.echannelingapplication.dto.ChatResponse;
import com.nibm.echannelling.echannelingapplication.dto.DoctorDTO;
import com.nibm.echannelling.echannelingapplication.entity.ChatHistory;
import com.nibm.echannelling.echannelingapplication.entity.User;
import com.nibm.echannelling.echannelingapplication.repository.ChatHistoryRepository;
import com.nibm.echannelling.echannelingapplication.repository.DoctorRepository;
import com.nibm.echannelling.echannelingapplication.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatbotService {

    private static final Logger logger = LoggerFactory.getLogger(ChatbotService.class);

    private final RestTemplate restTemplate;
    private final ChatHistoryRepository chatHistoryRepository;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;

    @Value("${openrouter.api.key}")
    private String openRouterApiKey;

    @Value("${openrouter.api.url}")
    private String openRouterApiUrl;

    public ChatbotService(RestTemplate restTemplate,
                          ChatHistoryRepository chatHistoryRepository,
                          UserRepository userRepository,
                          DoctorRepository doctorRepository) {
        this.restTemplate = restTemplate;
        this.chatHistoryRepository = chatHistoryRepository;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
    }

    public ChatResponse getChatResponse(ChatRequest chatRequest) {
        String symptoms = chatRequest.getSymptoms().trim().toLowerCase();
        logger.info("Processing chat request with symptoms: {} and patientId: {}", symptoms, chatRequest.getPatientId());

        // Detect non-symptom inputs
        String nonSymptomResponse = handleNonSymptomInput(symptoms);
        if (nonSymptomResponse != null) {
            // Return generic response without saving to chat history
            ChatResponse chatResponse = new ChatResponse();
            chatResponse.setSuggestedSpecialty(null);
            chatResponse.setResponse(nonSymptomResponse);
            chatResponse.setRecommendedDoctorIds(new Long[]{});
            return chatResponse;
        }

        // Construct prompt for symptom-related inputs
        String prompt = "You are a medical assistant for an e-channeling platform. A patient describes symptoms: " +
                chatRequest.getSymptoms() + ". Choose the most appropriate medical specialty from: Neurology, Cardiology, Dermatology, Ophthalmology, ENT, Pediatrics, Psychiatry, General Practice. " +
                "Guidelines: Use Pediatrics for symptoms involving 'baby' or 'child' (e.g., fever, coughing, ear-pulling). Use Neurology for migraines, numbness, seizures, dizziness, memory issues. " +
                "Use Cardiology for chest pain, irregular heartbeat, high blood pressure. Use Dermatology for rashes, acne, moles. Use Ophthalmology for blurry vision, dry eyes, black spots, flashes of light. " +
                "Use ENT for ear issues, sore throat, nosebleeds. Use Psychiatry for depression, anxiety, lack of motivation, sleep issues. Use General Practice for general or unspecified symptoms. " +
                "Respond ONLY with: Specialty: [specialty name] | Explanation: [brief explanation, max 100 words] | Immediate: [yes/no]. " +
                "Recommend immediate attention only for severe symptoms (e.g., high fever >103°F, breathing difficulty, chest pain, seizures).";

        // Prepare headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + openRouterApiKey);

        // Prepare body for completion model
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "deepseek/deepseek-r1:free");
        requestBody.put("prompt", prompt);
        requestBody.put("max_tokens", 200);

        // Make request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response;
        try {
            response = restTemplate.exchange(
                    openRouterApiUrl,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );
        } catch (Exception e) {
            logger.error("Error calling OpenRouter API: {}", e.getMessage());
            throw new RuntimeException("Failed to connect to AI service");
        }

        // Extract AI response
        String aiResponse = null;
        if (response.getBody() != null && response.getBody().get("choices") instanceof List) {
            List<?> choices = (List<?>) response.getBody().get("choices");
            if (!choices.isEmpty()) {
                Object firstChoice = choices.get(0);
                if (firstChoice instanceof Map) {
                    aiResponse = (String) ((Map<?, ?>) firstChoice).get("text");
                }
            }
        }

        // Fallback check
        if (aiResponse == null) {
            logger.error("AI response is null");
            throw new RuntimeException("AI response is null");
        }

        // Log AI response and parsed fields for debugging
        logger.info("Raw AI Response: {}", aiResponse);

        // Parse specialty, explanation, and immediate attention
        String specialty = "General Practice"; // Default fallback
        String explanation = "Symptoms require evaluation by a specialist.";
        boolean immediate = false;

        // Force specialty based on symptoms before parsing AI response
        if (symptoms.contains("baby") || symptoms.contains("child")) {
            specialty = "Pediatrics";
            explanation = "Symptoms in a baby or child require pediatric evaluation.";
        } else if (symptoms.contains("depressed") || symptoms.contains("anxiety") || symptoms.contains("motivation") ||
                symptoms.contains("sleep")) {
            specialty = "Psychiatry";
            explanation = "Mental health symptoms require psychiatric evaluation.";
        } else if (symptoms.contains("migraine") || symptoms.contains("numb") || symptoms.contains("seizure") ||
                symptoms.contains("dizzy") || symptoms.contains("memory")) {
            specialty = "Neurology";
            explanation = "Neurological symptoms require specialist evaluation.";
        } else if (symptoms.contains("chest pain") || symptoms.contains("heartbeat") || symptoms.contains("blood pressure")) {
            specialty = "Cardiology";
            explanation = "Cardiac symptoms require specialist evaluation.";
        } else if (symptoms.contains("rash") || symptoms.contains("acne") || symptoms.contains("mole")) {
            specialty = "Dermatology";
            explanation = "Skin symptoms require dermatological evaluation.";
        } else if (symptoms.contains("vision") || symptoms.contains("dry eyes") || symptoms.contains("black spots") ||
                symptoms.contains("flashes of light")) {
            specialty = "Ophthalmology";
            explanation = "Eye symptoms require ophthalmological evaluation.";
        } else if (symptoms.contains("ear") || symptoms.contains("sore throat") || symptoms.contains("nosebleed")) {
            specialty = "ENT";
            explanation = "Ear, nose, or throat symptoms require specialist evaluation.";
        } else if (aiResponse.contains("|")) {
            String[] parts = aiResponse.split("\\|");
            for (String part : parts) {
                part = part.trim();
                if (part.startsWith("Specialty:")) {
                    specialty = part.replace("Specialty:", "").trim();
                } else if (part.startsWith("Explanation:")) {
                    explanation = part.replace("Explanation:", "").trim();
                } else if (part.startsWith("Immediate:")) {
                    immediate = part.replace("Immediate:", "").trim().equalsIgnoreCase("yes");
                }
            }
        }

        // Log parsed fields
        logger.info("Parsed Specialty: {}", specialty);
        logger.info("Parsed Explanation: {}", explanation);
        logger.info("Parsed Immediate: {}", immediate);

        // Validate specialty
        List<String> validSpecialties = List.of("Neurology", "Cardiology", "Dermatology", "Ophthalmology", "ENT", "Pediatrics", "Psychiatry", "General Practice");
        if (!validSpecialties.contains(specialty)) {
            specialty = "General Practice";
            explanation = "Symptoms require evaluation by a general practitioner.";
        }

        // Fetch all verified doctors by specialty
        List<DoctorDTO> doctors = doctorRepository.findBySpecialty(specialty)
                .stream()
                .filter(doctor -> Boolean.TRUE.equals(doctor.getVerified()))
                .map(doctor -> {
                    DoctorDTO dto = new DoctorDTO();
                    dto.setId(doctor.getId());
                    dto.setName(doctor.getName());
                    dto.setSpecialty(doctor.getSpecialty());
                    dto.setAvailability(doctor.getAvailability());
                    dto.setEmail(doctor.getUser().getEmail());
                    dto.setVerified(doctor.getVerified());
                    return dto;
                })
                .collect(Collectors.toList());

        // Fallback to General Practice if no doctors found
        if (doctors.isEmpty()) {
            specialty = "General Practice";
            explanation = "No specialists available; consult a general practitioner.";
            doctors = doctorRepository.findBySpecialty(specialty)
                    .stream()
                    .filter(doctor -> Boolean.TRUE.equals(doctor.getVerified()))
                    .map(doctor -> {
                        DoctorDTO dto = new DoctorDTO();
                        dto.setId(doctor.getId());
                        dto.setName(doctor.getName());
                        dto.setSpecialty(doctor.getSpecialty());
                        dto.setAvailability(doctor.getAvailability());
                        dto.setEmail(doctor.getUser().getEmail());
                        dto.setVerified(doctor.getVerified());
                        return dto;
                    })
                    .collect(Collectors.toList());
        }

        // Format response
        StringBuilder formattedResponse = new StringBuilder();
        String patientContext = symptoms.contains("baby") || symptoms.contains("child")
                ? "your child's symptoms" : "your symptoms";
        formattedResponse.append("For ").append(patientContext)
                .append(", consult a ").append(specialty).append(" specialist");
        if (immediate) {
            formattedResponse.append(". Seek immediate care if symptoms worsen.");
        } else {
            formattedResponse.append(".");
        }

        if (!doctors.isEmpty()) {
            formattedResponse.append(" Available doctors:\n");
            for (DoctorDTO doctor : doctors) {
                formattedResponse.append("🩺 ").append(doctor.getName())
                        .append(" – ").append(doctor.getSpecialty())
                        .append("\n📅 ").append(doctor.getAvailability() != null ? doctor.getAvailability() : "Contact for availability")
                        .append(" | 📧 ").append(doctor.getEmail()).append("\n\n");
            }
        } else {
            formattedResponse.append(" No available doctors found for ").append(specialty).append(".");
        }

        // Truncate response to 1500 characters
        String finalResponse = formattedResponse.length() > 1500
                ? formattedResponse.substring(0, 1497) + "..."
                : formattedResponse.toString();

        // Save to DB only for authenticated users
        if (chatRequest.getPatientId() != null) {
            ChatHistory chatHistory = new ChatHistory();
            chatHistory.setMessage(chatRequest.getSymptoms());
            chatHistory.setResponse(finalResponse);

            Optional<User> userOptional = userRepository.findById(chatRequest.getPatientId());
            if (userOptional.isPresent()) {
                chatHistory.setUser(userOptional.get());
            } else {
                logger.error("Invalid patient ID: {}", chatRequest.getPatientId());
                throw new IllegalArgumentException("Invalid patient ID");
            }

            try {
                chatHistoryRepository.save(chatHistory);
            } catch (Exception e) {
                logger.error("Error saving chat history: {}", e.getMessage());
                throw new RuntimeException("Failed to save chat history");
            }
        } else {
            logger.info("Skipping chat history save for public access (patientId: null)");
        }

        // Prepare response DTO
        ChatResponse chatResponse = new ChatResponse();
        chatResponse.setSuggestedSpecialty(specialty);
        chatResponse.setResponse(finalResponse);
        chatResponse.setRecommendedDoctorIds(doctors.stream().map(DoctorDTO::getId).toArray(Long[]::new));

        return chatResponse;
    }

    private String handleNonSymptomInput(String symptoms) {
        // Define common non-symptom inputs
        if (symptoms.matches("hello|hi|hey|greetings")) {
            return "Hello! Please describe your symptoms for medical assistance.";
        } else if (symptoms.contains("web") || symptoms.contains("website") || symptoms.contains("details")) {
            return "Visit our website at www.echannelling.com for more information or describe your symptoms for medical help.";
        } else if (symptoms.isEmpty() || symptoms.matches("\\s*")) {
            return "Please provide your symptoms to receive medical recommendations.";
        } else if (symptoms.contains("how are you") || symptoms.contains("what's up")) {
            return "I'm here to help! Please share your symptoms for medical advice.";
        }
        // If not a clear non-symptom input, proceed with symptom processing
        return null;
    }

    public List<ChatHistory> getChatHistory(Long userId) {
        if (userId == null) {
            logger.info("Returning empty chat history for null userId (public access)");
            return List.of();
        }
        return chatHistoryRepository.findByUserId(userId);
    }
}