package MediAssist.controller;

import MediAssist.service.SymptomCheckerService;
import MediAssist.service.GroqService;
import MediAssist.service.MedicalSafetyService;
import MediAssist.service.MedicalTopicService;
import MediAssist.service.ChatHistoryService;
import MediAssist.model.ChatMessage;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin
public class ChatController {

    private final MedicalSafetyService medicalSafetyService;
    private final GroqService groqService;
    private final SymptomCheckerService symptomCheckerService;
    private final MedicalTopicService medicalTopicService;
    private final ChatHistoryService chatHistoryService;

    public ChatController(
        MedicalSafetyService medicalSafetyService,
        GroqService groqService,
        SymptomCheckerService symptomCheckerService,
        MedicalTopicService medicalTopicService,
        ChatHistoryService chatHistoryService) {

        this.medicalSafetyService = medicalSafetyService;
        this.groqService = groqService;
        this.symptomCheckerService = symptomCheckerService;
        this.medicalTopicService = medicalTopicService;
        this.chatHistoryService = chatHistoryService;
    }

    @PostMapping
    public String chat(
            @RequestBody String message,
            @RequestParam(defaultValue = "normal") String mode) {

        // Emergency check always comes first
        if (medicalSafetyService.isEmergency(message)) {
            return medicalSafetyService.getEmergencyResponse();
        }

        // Special medical modes are already medical
        if (!mode.equals("normal")) {
            return groqService.getResponse(message, mode);
        }

        // Normal chat must be medical
        if (!medicalTopicService.isMedicalTopic(message)) {

            return """
                    🏥 MediAssist

                    I'm a medical support assistant, so I can only help with
                    medical and health-related questions.

                    You can ask me about:
                    • Symptoms
                    • Diseases and health conditions
                    • Medicines
                    • General health information
                    • Medical appointments
                    • Prevention and healthy habits

                    Please ask me a medical or health-related question.
                    """;
        }

        return groqService.getResponse(message, mode);
    }

    @PostMapping("/symptoms")
    public String checkSymptoms(@RequestBody String symptoms) {

        if (medicalSafetyService.isEmergency(symptoms)) {
            return medicalSafetyService.getEmergencyResponse();
        }

        return symptomCheckerService.checkSymptoms(symptoms);
    }

    @PostMapping("/sessions/{sessionId}/messages")
public String sendMessageToSession(
        @PathVariable Long sessionId,
        @RequestParam Long userId,
        @RequestParam(defaultValue = "normal") String mode,
        @RequestBody String message) {

    chatHistoryService.getSessionForUser(
            sessionId,
            userId
    );

    chatHistoryService.saveMessage(
            sessionId,
            "user",
            message
    );

    // rest of your existing code...

        String response;

        // Check for emergency
        if (medicalSafetyService.isEmergency(message)) {

            response =
                    medicalSafetyService
                            .getEmergencyResponse();

        }

        // Symptom checker
        else if (mode.equals("symptoms")) {

            response =
                    symptomCheckerService
                            .checkSymptoms(message);

        }

        // Normal chat: allow only medical questions
        else if (mode.equals("normal")
                && !medicalTopicService
                .isMedicalTopic(message)) {

            response = """
                    🏥 MediAssist

                    I'm a medical support assistant, so I can only help with
                    medical and health-related questions.

                    Please ask me about symptoms, medicines, diseases,
                    healthcare, prevention, or general health information.
                    """;

        }

        // Send to AI
        else {

            response =
                    groqService
                            .getResponse(message, mode);
        }

        // Save MediAssist's response
        chatHistoryService.saveMessage(
                sessionId,
                "bot",
                response
        );

        return response;
    }
}