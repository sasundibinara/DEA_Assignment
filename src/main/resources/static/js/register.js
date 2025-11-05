// Toggle Password Visibility
function togglePassword() {
    const pwd = document.getElementById("password");
    if (!pwd) return;
    pwd.type = pwd.type === "password" ? "text" : "password";
}

document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("registerForm");
    const fullNameInput = document.getElementById("fullName");
    const emailInput = document.getElementById("email");
    const passwordInput = document.getElementById("password");
    const roleSelect = document.getElementById("role");

    const fullNameError = document.getElementById("fullNameError");
    const emailError = document.getElementById("emailError");
    const passwordError = document.getElementById("passwordError");
    const roleError = document.getElementById("roleError");

    // --- FRONT-END VALIDATION ---
    form.addEventListener("submit", function (e) {
        let valid = true;

        // Clear old errors
        if (fullNameError) fullNameError.textContent = "";
        if (emailError) emailError.textContent = "";
        if (passwordError) passwordError.textContent = "";
        if (roleError) roleError.textContent = "";

        [fullNameInput, emailInput, passwordInput, roleSelect].forEach(el => {
            if (el) el.classList.remove("input-error");
        });

        // Get values
        const fullNameValue = fullNameInput ? fullNameInput.value.trim() : "";
        const emailValue = emailInput ? emailInput.value.trim() : "";
        const passwordValue = passwordInput ? passwordInput.value.trim() : "";
        const roleValue = roleSelect ? roleSelect.value : "";

        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        // Validate Full Name
        if (!fullNameValue) {
            if (fullNameError) fullNameError.textContent = "Full name is required.";
            if (fullNameInput) fullNameInput.classList.add("input-error");
            valid = false;
        } else if (fullNameValue.length < 2) {
            if (fullNameError) fullNameError.textContent = "Full name must be at least 2 characters.";
            if (fullNameInput) fullNameInput.classList.add("input-error");
            valid = false;
        }

        // Validate Email
        if (!emailValue) {
            if (emailError) emailError.textContent = "Email is required.";
            if (emailInput) emailInput.classList.add("input-error");
            valid = false;
        } else if (!emailRegex.test(emailValue)) {
            if (emailError) emailError.textContent = "Please enter a valid email address.";
            if (emailInput) emailInput.classList.add("input-error");
            valid = false;
        }

        // Validate Password
        if (!passwordValue) {
            if (passwordError) passwordError.textContent = "Password is required.";
            if (passwordInput) passwordInput.classList.add("input-error");
            valid = false;
        } else if (passwordValue.length < 6) {
            if (passwordError) passwordError.textContent = "Password must be at least 6 characters.";
            if (passwordInput) passwordInput.classList.add("input-error");
            valid = false;
        } else if (!/(?=.*[a-z])(?=.*[A-Z])(?=.*\d)/.test(passwordValue)) {
            if (passwordError) passwordError.textContent = "Password must contain uppercase, lowercase, and number.";
            if (passwordInput) passwordInput.classList.add("input-error");
            valid = false;
        }

        // Validate Role
        if (!roleValue) {
            if (roleError) roleError.textContent = "Please select your role.";
            if (roleSelect) roleSelect.classList.add("input-error");
            valid = false;
        }

        if (!valid) e.preventDefault();
    });

    // --- ALERT HANDLER ---
    const params = new URLSearchParams(window.location.search);
    if (params.has("error")) {
        showAlert("Registration failed. Please try again.", "error");
    } else if (params.has("success")) {
        showAlert("Account created successfully!", "success");
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
    }, 3500);
}

// Real-time validation feedback
document.addEventListener("DOMContentLoaded", function() {
    const emailInput = document.getElementById("email");
    const passwordInput = document.getElementById("password");

    // Email validation on blur
    if (emailInput) {
        emailInput.addEventListener("blur", function() {
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            const emailError = document.getElementById("emailError");

            if (this.value && !emailRegex.test(this.value)) {
                if (emailError) emailError.textContent = "Please enter a valid email address.";
                this.classList.add("input-error");
            } else {
                if (emailError) emailError.textContent = "";
                this.classList.remove("input-error");
            }
        });
    }

    // Password strength indicator
    if (passwordInput) {
        passwordInput.addEventListener("input", function() {
            const value = this.value;
            let strengthText = "";
            let color = "";

            if (value.length === 0) {
                strengthText = "";
            } else if (value.length < 6) {
                strengthText = "Weak password";
                color = "#e53e3e";
            } else if (!/(?=.*[a-z])(?=.*[A-Z])(?=.*\d)/.test(value)) {
                strengthText = "Medium password";
                color = "#dd6b20";
            } else {
                strengthText = "Strong password";
                color = "#38a169";
            }

            // Remove existing strength indicator
            const existingStrength = this.parentElement.querySelector(".password-strength");
            if (existingStrength) {
                existingStrength.remove();
            }

            // Add new strength indicator
            if (strengthText) {
                const strength = document.createElement("div");
                strength.textContent = strengthText;
                strength.style.color = color;
                strength.className = "password-strength";
                this.parentElement.appendChild(strength);
            }
        });
    }
});