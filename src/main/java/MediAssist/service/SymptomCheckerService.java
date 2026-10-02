package MediAssist.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class SymptomCheckerService {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public SymptomCheckerService() {

        httpClient = HttpClient.newHttpClient();
        objectMapper = new ObjectMapper();
    }

    public String checkSymptoms(String symptoms) {

        try {

            String apiKey = System.getenv("GROQ_API_KEY");

            if (apiKey == null || apiKey.isBlank()) {
                return "Groq API key is not configured.";
            }

            String prompt = """
                    You are MediAssist's Symptom Checker.

                    Analyze the symptoms provided by the user and give
                    safe general medical information.

                    IMPORTANT SAFETY RULES:

                    - Never provide a definitive diagnosis.
                    - Never say the user definitely has a disease.
                    - Symptoms may have multiple possible causes.
                    - Do not prescribe medicines.
                    - Do not provide personalized medicine dosages.
                    - Identify warning signs that may require urgent care.
                    - Recommend professional medical care when appropriate.
                    - If the symptoms could indicate an emergency, clearly
                      tell the user to seek emergency medical attention.

                    FORMAT YOUR RESPONSE EXACTLY LIKE THIS:

                    🩺 Symptom Assessment

                    Possible explanations:
                    - Explain 2 to 4 reasonable possibilities when appropriate.

                    Urgency:
                    - State one of:
                      Low urgency
                      Needs medical attention
                      Urgent medical attention
                      Emergency

                    Warning signs:
                    - List important warning signs relevant to these symptoms.
                    - If none are particularly relevant, say:
                      No specific emergency warning signs identified from the
                      information provided.

                    What you can do now:
                    - Give safe general steps.

                    When to see a doctor:
                    - Explain when the user should contact a healthcare professional.

                    IMPORTANT:
                    - Do not use Markdown headings.
                    - Do not use ** symbols.
                    - Keep each section on separate lines.
                    - Use simple bullet points.
                    - Do not diagnose the user.

                    User symptoms:
                    """ + symptoms;

            String requestBody = """
                    {
                        "model": "openai/gpt-oss-20b",
                        "messages": [
                            {
                                "role": "system",
                                "content": "You are a safe medical symptom assessment assistant. Never provide a definitive diagnosis or prescribe medicines or dosages."
                            },
                            {
                                "role": "user",
                                "content": %s
                            }
                        ]
                    }
                    """.formatted(
                    objectMapper.writeValueAsString(prompt)
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
                        "Groq Symptom Checker Error: "
                                + response.statusCode()
                );

                System.out.println(response.body());

                return "Sorry, I couldn't analyze the symptoms right now.";
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

            return "Sorry, something went wrong while analyzing the symptoms.";
        }
    }
}