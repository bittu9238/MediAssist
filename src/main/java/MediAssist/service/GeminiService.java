package MediAssist.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.stereotype.Service;

@Service
public class GeminiService {

    private final Client client;

    public GeminiService() {

        String apiKey = System.getenv("GEMINI_API_KEY");

        client = Client.builder()
                .apiKey(apiKey)
                .build();
    }

    public String getResponse(String message) {

        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-3.8-flash",
                        message,
                        null
                );

        return response.text();
    }
}