function togglePassword() {
    const pwd = document.getElementById("password");
    if (!pwd) return;
    pwd.type = pwd.type === "password" ? "text" : "password";
}

document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("loginForm");
    const emailInput = document.getElementById("email");
    const passwordInput = document.getElementById("password");
    const roleSelect = document.getElementById("role");

    const emailError = document.getElementById("emailError");
    const passwordError = document.getElementById("passwordError");
    const roleError = document.getElementById("roleError");

    // --- FRONT-END VALIDATION ---
    form.addEventListener("submit", function (e) {
        let valid = true;

        // Clear old errors
        emailError.textContent = "";
        passwordError.textContent = "";
        roleError.textContent = "";
        [emailInput, passwordInput, roleSelect].forEach(el => el.classList.remove("input-error"));

        // Email
        const emailValue = emailInput.value.trim();
        const passwordValue = passwordInput.value.trim();
        const roleValue = roleSelect.value;

        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        if (!emailValue) {
            emailError.textContent = "Email is required.";
            emailInput.classList.add("input-error");
            valid = false;
        } else if (!emailRegex.test(emailValue)) {
            emailError.textContent = "Please enter a valid email.";
            emailInput.classList.add("input-error");
            valid = false;
        }

        if (!roleValue) {
            roleError.textContent = "Please select your role.";
            roleSelect.classList.add("input-error");
            valid = false;
        }

        if (!passwordValue) {
            passwordError.textContent = "Password is required.";
            passwordInput.classList.add("input-error");
            valid = false;
        } else if (passwordValue.length < 6) {
            passwordError.textContent = "Password must be at least 6 characters.";
            passwordInput.classList.add("input-error");
            valid = false;
        }

        if (!valid) e.preventDefault();
    });

    // --- ALERT HANDLER ---
    const params = new URLSearchParams(window.location.search);
    if (params.has("error")) {
        showAlert("Invalid email or password.", "error");
    } else if (params.has("success")) {
        showAlert("Login successful!", "success");
    }
});

// --- CUSTOM ALERT FUNCTION ---
function showAlert(message, type) {
    const alertBox = document.createElement("div");
    alertBox.className = `custom-alert ${type}`;
    alertBox.textContent = message;
    document.body.appendChild(alertBox);

    setTimeout(() => alertBox.classList.add("visible"), 50);
    setTimeout(() => {
        alertBox.classList.remove("visible");
        setTimeout(() => alertBox.remove(), 300);
    }, 3000);
}
