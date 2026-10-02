let chatMode = "normal";
let currentSessionId = null;
async function sendMessage() {

    const input =
        document.getElementById("userMessage");

    const message =
        input.value.trim();

    if (message === "") {
        return;
    }

    if (currentSessionId === null) {
    await createNewChat();
}

    addMessage(message, "user");

    input.value = "";

    // Show temporary loading message
    const loadingId =
        "loading-" + Date.now();

    addMessage(
        "🏥 MediAssist is thinking...",
        "bot",
        loadingId
    );

    try {

        let endpoint =
        "/api/chat/sessions/"
        + currentSessionId
        + "/messages?mode="
        + encodeURIComponent(chatMode);

        const response =
            await fetch(endpoint, {

                method: "POST",

                headers: {
                    "Content-Type": "text/plain"
                },

                body: message
            });

        const reply =
            await response.text();

        // Remove loading message
        const loadingMessage =
            document.getElementById(loadingId);

        if (loadingMessage) {
            loadingMessage.remove();
        }

        addMessage(reply, "bot");

    } catch (error) {

        console.error(error);

        const loadingMessage =
            document.getElementById(loadingId);

        if (loadingMessage) {
            loadingMessage.remove();
        }

        addMessage(
            "Sorry, I couldn't connect to the MediAssist server.",
            "bot"
        );
    }
}


function addMessage(message, sender, id = null) {

    const chatMessages =
        document.getElementById("chatMessages");

    const messageDiv =
        document.createElement("div");

    messageDiv.classList.add(
        "message",
        sender
    );

    if (id !== null) {
        messageDiv.id = id;
    }

    // Preserve line breaks and spacing
    messageDiv.style.whiteSpace = "pre-wrap";

    messageDiv.textContent = message;

    chatMessages.appendChild(messageDiv);

    // Scroll to newest message
    chatMessages.scrollTo({
        top: chatMessages.scrollHeight,
        behavior: "smooth"
    });
}


async function symptomChecker() {

    chatMode = "symptoms";

    await createNewChat();

    const chatMessages =
        document.getElementById("chatMessages");

    addMessage(
        "🩺 Symptom Checker",
        "bot"
    );

    addMessage(
        "Please describe your symptoms, how long you have had them, and anything else you think is relevant.",
        "bot"
    );

    addMessage(
        "I can provide possible explanations and general urgency guidance, but I cannot provide a definitive diagnosis.",
        "bot"
    );

    document
        .getElementById("resultSection")
        .scrollIntoView({
            behavior: "smooth"
        });
}


function findHospitals() {

    chatMode = "normal";

    document
        .getElementById("resultSection")
        .scrollIntoView({
            behavior: "smooth"
        });

    addMessage(
        "🏥 Find Nearby Hospitals",
        "bot"
    );

    addMessage(
        "I need your location to find hospitals near you.",
        "bot"
    );

    if (!navigator.geolocation) {

        addMessage(
            "Sorry, your browser does not support location services.",
            "bot"
        );

        return;
    }

    navigator.geolocation.getCurrentPosition(

        function(position) {

            const latitude =
                position.coords.latitude;

            const longitude =
                position.coords.longitude;

            const mapsUrl =
                "https://www.google.com/maps/search/hospitals/@"
                + latitude
                + ","
                + longitude
                + ",14z";

            addMessage(
                "📍 Your location was found. You can search nearby hospitals using the button below.",
                "bot"
            );

            const button =
                document.createElement("button");

            button.textContent =
                "🏥 Find Nearby Hospitals";

            button.className =
                "history-toggle";

            button.style.marginTop =
                "10px";

            button.onclick = function() {

                window.open(
                    mapsUrl,
                    "_blank"
                );

            };

            document
                .getElementById("chatMessages")
                .appendChild(button);

        },

        function(error) {

            console.error(error);

            addMessage(
                "📍 I couldn't access your location. Please allow location access in your browser and try again.",
                "bot"
            );
        }
    );
}

function openEmergency() {

    const resultSection =
        document.getElementById("resultSection");

    resultSection.scrollIntoView({
        behavior: "smooth"
    });

    const chatMessages =
        document.getElementById("chatMessages");

    chatMessages.innerHTML = "";

    addMessage(
        "🚨 EMERGENCY MEDICAL ASSISTANCE",
        "bot"
    );

    addMessage(
        "If you are experiencing severe chest pain, difficulty breathing, severe bleeding, loss of consciousness, stroke symptoms, or another life-threatening emergency, seek immediate medical help.",
        "bot"
    );

    const emergencyLink =
        document.createElement("a");

    emergencyLink.href = "tel:112";
    emergencyLink.textContent =
        "📞 Call Emergency Services (112)";

    emergencyLink.style.display = "inline-block";
    emergencyLink.style.marginTop = "10px";
    emergencyLink.style.padding = "12px 18px";
    emergencyLink.style.background = "#dc2626";
    emergencyLink.style.color = "white";
    emergencyLink.style.borderRadius = "8px";
    emergencyLink.style.textDecoration = "none";
    emergencyLink.style.fontWeight = "bold";

    chatMessages.appendChild(emergencyLink);

    const hospitalButton =
        document.createElement("button");

    hospitalButton.textContent =
        "🏥 Find Nearest Emergency Hospital";

    hospitalButton.style.display = "block";
    hospitalButton.style.marginTop = "10px";
    hospitalButton.style.padding = "12px 18px";
    hospitalButton.style.background = "#2563eb";
    hospitalButton.style.color = "white";
    hospitalButton.style.border = "none";
    hospitalButton.style.borderRadius = "8px";
    hospitalButton.style.cursor = "pointer";
    hospitalButton.style.fontWeight = "bold";

    hospitalButton.onclick = function() {
        findHospitals();
    };

    chatMessages.appendChild(hospitalButton);
}


async function medicineInfo() {

    chatMode = "medicine";

    await createNewChat();

    const resultSection =
        document.getElementById("resultSection");

    resultSection.scrollIntoView({
        behavior: "smooth"
    });

    const chatMessages =
        document.getElementById("chatMessages");

    chatMessages.innerHTML = "";

    addMessage(
        "💊 Medicine Information",
        "bot"
    );

    addMessage(
        "Tell me the name of a medicine, and I can provide general information about its common uses, purpose, and important precautions. I cannot prescribe medicines or provide personalized dosages.",
        "bot"
    );
    document
        .getElementById("resultSection")
        .scrollIntoView({
            behavior: "smooth"
        });
}



async function appointmentHelp() {

    chatMode = "appointment";

    await createNewChat();


    const resultSection =
        document.getElementById("resultSection");

    resultSection.scrollIntoView({
        behavior: "smooth"
    });

    const chatMessages =
        document.getElementById("chatMessages");

    chatMessages.innerHTML = "";

    addMessage(
        "📅 Appointment Assistance",
        "bot"
    );

    addMessage(
        "I can help you prepare for a healthcare appointment. You can tell me your symptoms, how long you have had them, medicines you currently take, or what you want to ask your doctor.",
        "bot"
    );

    addMessage(
        "Please remember: I can help you organize information and questions, but I cannot replace a healthcare professional.",
        "bot"
    );
document
        .getElementById("resultSection")
        .scrollIntoView({
            behavior: "smooth"
        });
}


async function healthInformation() {

    chatMode = "health";

    await createNewChat();

    const resultSection =
        document.getElementById("resultSection");

    resultSection.scrollIntoView({
        behavior: "smooth"
    });

    const chatMessages =
        document.getElementById("chatMessages");

    chatMessages.innerHTML = "";

    addMessage(
        "❤️ Health Information",
        "bot"
    );

    addMessage(
        "Ask me about health, prevention, common medical conditions, symptoms, healthy habits, or general medical information.",
        "bot"
    );

    addMessage(
        "I provide general health information and cannot provide a definitive diagnosis or replace professional medical advice.",
        "bot"
    );
document
        .getElementById("resultSection")
        .scrollIntoView({
            behavior: "smooth"
        });
}


async function openNormalChat() {

    chatMode = "normal";

    await createNewChat();

    document
        .getElementById("resultSection")
        .scrollIntoView({
            behavior: "smooth"
        });
}

async function createNewChat() {

    try {

        const storedUser =
            localStorage.getItem("mediassistUser");

        if (!storedUser) {

            window.location.href = "/login.html";

            return;
        }

        const user =
            JSON.parse(storedUser);

        const response = await fetch(
            "/api/history/sessions?userId="
            + user.id
            + "&mode="
            + encodeURIComponent(chatMode),
            {
                method: "POST"
            }
        );

        if (!response.ok) {

            throw new Error(
                "Could not create chat"
            );
        }

        const session =
            await response.json();

        currentSessionId =
            session.id;

        document
            .getElementById("chatMessages")
            .innerHTML = "";

        addMessage(
            "🏥 MediAssist\n\nHow can I help you with your health today?",
            "bot"
        );

        loadChatHistory();

    } catch (error) {

        console.error(error);

        addMessage(
            "Sorry, I couldn't create a new chat.",
            "bot"
        );
    }
}


async function loadChatHistory() {

    try {

        const storedUser =
    localStorage.getItem("mediassistUser");

if (!storedUser) {

    window.location.href = "/login.html";

    return;
}

const user =
    JSON.parse(storedUser);

    const response = await fetch(
    "/api/history/sessions?userId="
    + user.id
    + "&mode="
    + encodeURIComponent(chatMode)
    );

        const sessions = await response.json();

        const historyList =
            document.getElementById("chatHistoryList");

        const historyPanel =
            document.getElementById("chatHistoryPanel");

        historyPanel.style.display = "none";

        historyList.innerHTML = "";

        if (sessions.length === 0) {

            historyList.innerHTML =
                "<p>No previous chats yet.</p>";

            return;
        }

        sessions.forEach(session => {

            const chatItem =
                document.createElement("button");

            chatItem.className = "history-item";

            chatItem.textContent =
                session.title;

            chatItem.onclick = function() {

                loadChatSession(session.id);

            };

            historyList.appendChild(chatItem);

        });

    } catch (error) {

        console.error(error);

    }
}

async function loadChatSession(sessionId) {

    try {

        const storedUser =
            localStorage.getItem("mediassistUser");

        if (!storedUser) {

            window.location.href = "/login.html";

            return;
        }

        const user =
            JSON.parse(storedUser);

        const response = await fetch(
            "/api/history/sessions/"
            + sessionId
            + "/messages?userId="
            + user.id
        );

        if (!response.ok) {

            throw new Error(
                "Could not load chat"
            );
        }

        const messages =
            await response.json();

        currentSessionId =
            sessionId;

        const chatMessages =
            document.getElementById(
                "chatMessages"
            );

        chatMessages.innerHTML = "";

        messages.forEach(message => {

            addMessage(
                message.message,
                message.sender
            );

        });

        document
            .getElementById("resultSection")
            .scrollIntoView({
                behavior: "smooth"
            });

    } catch (error) {

        console.error(error);

        addMessage(
            "Sorry, I couldn't load this chat.",
            "bot"
        );
    }
}

window.addEventListener("load", function() {

    loadChatHistory();

});

function toggleChatHistory() {

    const historyPanel =
        document.getElementById("chatHistoryPanel");

    if (historyPanel.style.display === "none") {

        historyPanel.style.display = "block";

    } else {

        historyPanel.style.display = "none";

    }
}