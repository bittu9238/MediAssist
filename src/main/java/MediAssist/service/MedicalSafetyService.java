package MediAssist.service;

import org.springframework.stereotype.Service;

@Service
public class MedicalSafetyService {

    public boolean isEmergency(String message) {

    System.out.println("MESSAGE RECEIVED BY JAVA: [" + message + "]");

    String text = message.toLowerCase();

    System.out.println("MESSAGE AFTER LOWERCASE: [" + text + "]");

        if (text.contains("chest pain")
                || text.contains("difficulty breathing")
                || text.contains("can't breathe")
                || text.contains("cannot breathe")
                || text.contains("severe bleeding")
                || text.contains("unconscious")
                || text.contains("stroke")) {

            return true;
        }

        return false;
    }

    public String getEmergencyResponse() {

        return "This may require urgent medical attention. "
                + "Please contact your local emergency service "
                + "or go to the nearest emergency department immediately.";
    }
}