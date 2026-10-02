package MediAssist.service;

import org.springframework.stereotype.Service;

@Service
public class MedicalTopicService {

    private final String[] medicalKeywords = {

            // General health
            "health",
            "healthy",
            "disease",
            "condition",
            "illness",
            "symptom",
            "symptoms",
            "pain",
            "fever",
            "infection",
            "virus",
            "bacteria",

            // Body
            "heart",
            "chest",
            "lung",
            "lungs",
            "brain",
            "stomach",
            "kidney",
            "liver",
            "blood",
            "bone",
            "skin",
            "eye",
            "ear",
            "throat",
            "head",
            "muscle",
            "joint",

            // Common symptoms
            "cough",
            "cold",
            "headache",
            "vomiting",
            "nausea",
            "diarrhea",
            "dizziness",
            "weakness",
            "fatigue",
            "breathing",
            "breath",
            "bleeding",
            "swelling",
            "rash",
            "itching",
            "sore",
            "painful",

            // Medical professionals/facilities
            "doctor",
            "hospital",
            "clinic",
            "nurse",
            "surgeon",
            "dentist",
            "therapist",
            "appointment",
            "emergency",

            // Medicine
            "medicine",
            "medication",
            "tablet",
            "capsule",
            "drug",
            "antibiotic",
            "prescription",
            "dosage",
            "dose",

            // Health topics
            "blood pressure",
            "diabetes",
            "thyroid",
            "cancer",
            "asthma",
            "allergy",
            "anxiety",
            "depression",
            "stress",
            "sleep",
            "nutrition",
            "diet",
            "exercise",
            "pregnancy",
            "pregnant",
            "vitamin",
            "weight",
            "obesity",
            "cholesterol",

            // Emergency/accident
            "accident",
            "injury",
            "unconscious",
            "stroke",
            "seizure",
            "burn"
    };

    public boolean isMedicalTopic(String message) {

        String text = message.toLowerCase();

        for (String keyword : medicalKeywords) {

            if (text.contains(keyword)) {
                return true;
            }
        }

        return false;
    }
}