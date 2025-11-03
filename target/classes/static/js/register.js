document.addEventListener('DOMContentLoaded', function() {
    const registerForm = document.getElementById('registerForm');
    const fullNameInput = document.getElementById('fullName');
    const emailInput = document.getElementById('email');
    const passwordInput = document.getElementById('password');
    const roleSelect = document.getElementById('role');
    const togglePasswordBtn = document.getElementById('togglePassword');
    const registerBtn = registerForm.querySelector('.btn-register');

    // Password Toggle Functionality
    if (togglePasswordBtn) {
        togglePasswordBtn.addEventListener('click', function() {
            const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
            passwordInput.setAttribute('type', type);

            const icon = this.querySelector('i');
            if (type === 'text') {
                icon.classList.remove('fa-eye');
                icon.classList.add('fa-eye-slash');
            } else {
                icon.classList.remove('fa-eye-slash');
                icon.classList.add('fa-eye');
            }
        });
    }

    // Email Validation Function
    function validateEmail(email) {
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(String(email).toLowerCase());
    }

    // Real-time Validation
    fullNameInput.addEventListener('blur', function() {
        if (this.value.trim().length < 2) {
            this.classList.add('is-invalid');
        } else {
            this.classList.remove('is-invalid');
        }
    });

    fullNameInput.addEventListener('input', function() {
        if (this.classList.contains('is-invalid') && this.value.trim().length >= 2) {
            this.classList.remove('is-invalid');
        }
    });

    emailInput.addEventListener('blur', function() {
        if (!validateEmail(this.value)) {
            this.classList.add('is-invalid');
        } else {
            this.classList.remove('is-invalid');
        }
    });

    emailInput.addEventListener('input', function() {
        if (this.classList.contains('is-invalid') && validateEmail(this.value)) {
            this.classList.remove('is-invalid');
        }
    });

    passwordInput.addEventListener('blur', function() {
        if (this.value.length < 8) {
            this.classList.add('is-invalid');
        } else {
            this.classList.remove('is-invalid');
        }
    });

    passwordInput.addEventListener('input', function() {
        if (this.classList.contains('is-invalid') && this.value.length >= 8) {
            this.classList.remove('is-invalid');
        }
    });

    // Form Submission Handler
    registerForm.addEventListener('submit', function(e) {
        // Basic client-side validation before server submission
        let isValid = true;

        // Validate Full Name
        if (fullNameInput.value.trim().length < 2) {
            fullNameInput.classList.add('is-invalid');
            isValid = false;
        }

        // Validate Email
        if (!validateEmail(emailInput.value)) {
            emailInput.classList.add('is-invalid');
            isValid = false;
        }

        // Validate Password
        if (passwordInput.value.length < 8) {
            passwordInput.classList.add('is-invalid');
            isValid = false;
        }

        // Validate Role
        if (!roleSelect.value) {
            roleSelect.classList.add('is-invalid');
            isValid = false;
        }

        // If client-side validation fails, prevent submission
        if (!isValid) {
            e.preventDefault();
            showToast('Please fill in all fields correctly', 'error');
            return false;
        }

        // Show loading state
        registerBtn.classList.add('loading');
        registerBtn.disabled = true;

        // Form will be submitted to server
        // Loading state will be visible during server processing
    });

    // Toast Notification Function
    function showToast(message, type = 'success') {
        const toast = document.createElement('div');
        toast.className = `toast-notification toast-${type}`;
        toast.innerHTML = `
            <i class="fas fa-${type === 'success' ? 'check-circle' : 'exclamation-circle'}"></i>
            <span>${message}</span>
        `;

        Object.assign(toast.style, {
            position: 'fixed',
            top: '20px',
            right: '20px',
            padding: '16px 24px',
            background: type === 'success' ? '#48bb78' : '#f56565',
            color: 'white',
            borderRadius: '8px',
            boxShadow: '0 4px 12px rgba(0,0,0,0.15)',
            zIndex: '10000',
            display: 'flex',
            alignItems: 'center',
            gap: '10px',
            animation: 'slideInRight 0.3s ease',
            fontSize: '14px',
            fontWeight: '500'
        });

        document.body.appendChild(toast);

        setTimeout(() => {
            toast.style.animation = 'slideOutRight 0.3s ease';
            setTimeout(() => toast.remove(), 300);
        }, 3000);
    }

    // Add CSS animations for toast
    const style = document.createElement('style');
    style.textContent = `
        @keyframes slideInRight {
            from {
                transform: translateX(100%);
                opacity: 0;
            }
            to {
                transform: translateX(0);
                opacity: 1;
            }
        }
        @keyframes slideOutRight {
            from {
                transform: translateX(0);
                opacity: 1;
            }
            to {
                transform: translateX(100%);
                opacity: 0;
            }
        }
    `;
    document.head.appendChild(style);

    // Add focus animations
    const formInputs = document.querySelectorAll('.form-control, .form-select');
    formInputs.forEach(input => {
        input.addEventListener('focus', function() {
            this.style.transform = 'scale(1.01)';
            this.style.transition = 'transform 0.2s ease';
        });

        input.addEventListener('blur', function() {
            this.style.transform = 'scale(1)';
        });
    });

    // Handle server-side validation errors (if any exist on page load)
    const errorMessages = document.querySelectorAll('.error-message');
    if (errorMessages.length > 0) {
        errorMessages.forEach(error => {
            const input = error.previousElementSibling;
            if (input && input.classList.contains('form-control')) {
                input.classList.add('is-invalid');
            }
        });
    }

    // Console welcome message
    console.log('%c Welcome to PaperHub Registration! ',
        'background: linear-gradient(135deg, #2196f3 0%, #1976d2 100%); color: white; font-size: 16px; padding: 8px; border-radius: 4px;'
    );
});