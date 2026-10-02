package MediAssist.service;

import org.springframework.stereotype.Service;

@Service
public class MedicalResponseService {

    public String getResponse(String message) {

        String text = message.toLowerCase();


        // Headache

        if (text.contains("headache")) {

            return """
                    Headaches can have many causes,
                    including stress, dehydration,
                    lack of sleep, or illness.

                    Seek urgent medical care if you have:
                    • Severe sudden headache
                    • Weakness
                    • Confusion
                    • Vision changes

                    This is general information and not a diagnosis.
                    """;
        }


        // Fever

        if (text.contains("fever")) {

            return """
                    Fever is often caused by infections.

                    Rest, stay hydrated, and monitor symptoms.

                    Seek medical care if:
                    • Fever is very high
                    • Difficulty breathing occurs
                    • Symptoms worsen

                    This is general information and not a diagnosis.
                    """;
        }


        // Cough

        if (text.contains("cough")) {

            return """
                    Coughs can occur with colds,
                    allergies, or infections.

                    Seek medical attention if:
                    • Difficulty breathing
                    • Chest pain
                    • Coughing blood

                    This is general information and not a diagnosis.
                    """;
        }


        // Default

        return """
                Thank you for your message.

                MediAssist currently supports
                general medical information.

                Please describe:
                • Symptoms
                • Duration
                • Severity

                This is not a medical diagnosis.
                """;
    }
}