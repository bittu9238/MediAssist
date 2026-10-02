package MediAssist.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class GroqService {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public GroqService() {

        httpClient = HttpClient.newHttpClient();
        objectMapper = new ObjectMapper();
    }

    public String getResponse(String message, String mode) {

        try {

            String apiKey = System.getenv("GROQ_API_KEY");

            if (apiKey == null || apiKey.isBlank()) {
                return "Groq API key is not configured.";
            }

            String modeInstructions = getModeInstructions(mode);

            String systemPrompt = """
                    You are MediAssist, a medical support assistant.

                    Your purpose is to provide safe, general medical and health information.

                    IMPORTANT SAFETY RULES:

                    - Do not provide a definitive diagnosis.
                    - Do not claim that the user definitely has a disease.
                    - Symptoms can have multiple possible causes.
                    - Do not prescribe medicines.
                    - Do not provide personalized medicine dosages.
                    - Clearly mention warning signs when relevant.
                    - Recommend professional medical care when appropriate.
                    - If the situation may be an emergency, recommend urgent medical attention.
                    - Do not answer questions unrelated to medicine, health, healthcare, or medical support.
                    - Do not pretend to be a doctor.

                    RESPONSE FORMAT:

                    Use clear headings and bullet points.

                    Possible explanations:
                    - Explain relevant possibilities when appropriate.

                    Warning signs:
                    - Mention important warning signs when relevant.

                    What to do:
                    - Give safe general next steps.

                    When to seek medical care:
                    - Explain when professional care may be needed.

                    Do not use Markdown symbols such as # or **.
                    Keep the response easy to read.

                    MODE:
                    """ + modeInstructions;

            String requestBody = """
                    {
                        "model": "openai/gpt-oss-20b",
                        "messages": [
                            {
                                "role": "system",
                                "content": %s
                            },
                            {
                                "role": "user",
                                "content": %s
                            }
                        ]
                    }
                    """.formatted(
                    objectMapper.writeValueAsString(systemPrompt),
                    objectMapper.writeValueAsString(message)
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(
                            "https://api.groq.com/openai/v1/chat/completions"
                    ))
                    .header(
                            "Authorization",
                            "Bearer " + apiKey
                    )
                    .header(
                            "Content-Type",
                            "application/json"
                    )
                    .POST(
                            HttpRequest.BodyPublishers.ofString(requestBody)
                    )
                    .build();

            HttpResponse<String> response =
                    httpClient.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            if (response.statusCode() != 200) {

                System.out.println(
                        "Groq API Error: "
                                + response.statusCode()
                );

                System.out.println(response.body());

                return "Sorry, I couldn't get a response from the AI service.";
            }

            JsonNode jsonResponse =
                    objectMapper.readTree(response.body());

            return jsonResponse
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

        } catch (Exception e) {

            e.printStackTrace();

            return "Sorry, something went wrong while connecting to the AI service.";
        }
    }

    private String getModeInstructions(String mode) {

        return switch (mode) {

            case "medicine" ->
                    """
                    The user is asking about medicine.
                    Provide general information about the medicine,
                    its common purpose and important precautions.
                    Do not prescribe it or give personalized dosage instructions.
                    """;

            case "appointment" ->
                    """
                    The user wants help preparing for a medical appointment.
                    Help them organize symptoms, medical history,
                    questions for the doctor, and information they may want to bring.
                    """;

            case "health" ->
                    """
                    The user wants general health information.
                    Explain health topics, prevention, healthy habits,
                    common conditions, and general medical information.
                    """;

            default ->
                    """
                    The user is using the normal medical support chat.
                    Answer their medical or health-related question safely.
                    """;
        };
    }
}