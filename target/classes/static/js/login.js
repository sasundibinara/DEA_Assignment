document.addEventListener('DOMContentLoaded', function() {

    // Get form elements
    const loginForm = document.getElementById('loginForm');
    const emailInput = document.getElementById('email');
    const passwordInput = document.getElementById('password');
    const togglePasswordBtn = document.getElementById('togglePassword');
    const loginBtn = loginForm.querySelector('.btn-login');
    const btnLoader = loginBtn.querySelector('.btn-loader');
    const rememberMeCheckbox = document.getElementById('rememberMe');

    // Load saved email if exists
    loadSavedCredentials();

    // Password Toggle Functionality
    togglePasswordBtn.addEventListener('click', function() {
        const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
        passwordInput.setAttribute('type', type);

        // Toggle icon
        const icon = this.querySelector('i');
        if (type === 'text') {
            icon.classList.remove('fa-eye');
            icon.classList.add('fa-eye-slash');
        } else {
            icon.classList.remove('fa-eye-slash');
            icon.classList.add('fa-eye');
        }
    });


    // Email Validation
    function validateEmail(email) {
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(String(email).toLowerCase());
    }

    // Real-time Email Validation
    emailInput.addEventListener('blur', function() {
        if (this.value && !validateEmail(this.value)) {
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

    // Real-time Password Validation
    passwordInput.addEventListener('blur', function() {
        if (!this.value) {
            this.classList.add('is-invalid');
        } else {
            this.classList.remove('is-invalid');
        }
    });

    passwordInput.addEventListener('input', function() {
        if (this.classList.contains('is-invalid') && this.value) {
            this.classList.remove('is-invalid');
        }
    });

    // Form Submission
    loginForm.addEventListener('submit', function(e) {
        e.preventDefault();

        // Reset validation states
        let isValid = true;

        // Validate Email
        if (!emailInput.value) {
            emailInput.classList.add('is-invalid');
            emailInput.classList.add('error');
            isValid = false;
            setTimeout(() => emailInput.classList.remove('error'), 500);
        } else if (!validateEmail(emailInput.value)) {
            emailInput.classList.add('is-invalid');
            emailInput.classList.add('error');
            isValid = false;
            setTimeout(() => emailInput.classList.remove('error'), 500);
        } else {
            emailInput.classList.remove('is-invalid');
        }

        // Validate Password
        if (!passwordInput.value) {
            passwordInput.classList.add('is-invalid');
            passwordInput.classList.add('error');
            isValid = false;
            setTimeout(() => passwordInput.classList.remove('error'), 500);
        } else {
            passwordInput.classList.remove('is-invalid');
        }

        // If form is valid, proceed with login
        if (isValid) {
            performLogin();
        }
    });

    // Save credentials if remember me is checked
    function saveCredentials() {
        if (rememberMeCheckbox.checked) {
            localStorage.setItem('rememberedEmail', emailInput.value);
            localStorage.setItem('rememberMe', 'true');
        } else {
            localStorage.removeItem('rememberedEmail');
            localStorage.removeItem('rememberMe');
        }
    }

    // Load saved credentials
    function loadSavedCredentials() {
        const rememberedEmail = localStorage.getItem('rememberedEmail');
        const rememberMe = localStorage.getItem('rememberMe');

        if (rememberMe === 'true' && rememberedEmail) {
            emailInput.value = rememberedEmail;
            rememberMeCheckbox.checked = true;
        }
    }

    // Simulate Login Process
    function performLogin() {
        // Save credentials if checked
        saveCredentials();

        // Show loading state
        loginBtn.classList.add('loading');
        btnLoader.style.display = 'inline-block';
        loginBtn.innerHTML = '<span class="btn-loader"><i class="fas fa-spinner fa-spin"></i></span> Logging in...';

        // Disable form inputs
        emailInput.disabled = true;
        passwordInput.disabled = true;

        // Simulate API call with setTimeout
        setTimeout(() => {
            // Success state
            loginBtn.classList.remove('loading');
            loginBtn.classList.add('success');
            loginBtn.innerHTML = '<i class="fas fa-check"></i> Success!';

            // Show success toast notification
            showToast('Login successful! Redirecting...', 'success');

            // Redirect after short delay
            setTimeout(() => {
                window.location.href = '../papers/list.html';
            }, 1000);

        }, 1500); // Simulated delay
    }

    // Toast Notification Function
    function showToast(message, type = 'success') {
        // Create toast element
        const toast = document.createElement('div');
        toast.className = `toast-notification toast-${type}`;
        toast.innerHTML = `
            <i class="fas fa-${type === 'success' ? 'check-circle' : 'exclamation-circle'}"></i>
            <span>${message}</span>
        `;

        // Add styles
        Object.assign(toast.style, {
            position: 'fixed',
            top: '20px',
            right: '20px',
            padding: '16px 24px',
            background: type === 'success' ? '#4caf50' : '#f44336',
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

        // Remove after 3 seconds
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

    // Enter key support for form submission
    emailInput.addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            passwordInput.focus();
        }
    });

    passwordInput.addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            loginForm.dispatchEvent(new Event('submit'));
        }
    });

    // Add focus animations
    const formInputs = document.querySelectorAll('.form-input');
    formInputs.forEach(input => {
        input.addEventListener('focus', function() {
            this.parentElement.style.transform = 'scale(1.01)';
            this.parentElement.style.transition = 'transform 0.2s ease';
        });

        input.addEventListener('blur', function() {
            this.parentElement.style.transform = 'scale(1)';
        });
    });

    // Forgot password link handler
    const forgotLink = document.querySelector('.forgot-link');
    if (forgotLink) {
        forgotLink.addEventListener('click', function(e) {
            e.preventDefault();
            showToast('Password reset feature coming soon!', 'info');
        });
    }

    // Console welcome message
    console.log('%c Welcome to PaperHub Login! ',
        'background: linear-gradient(135deg, #2196f3 0%, #1976d2 100%); color: white; font-size: 16px; padding: 8px; border-radius: 4px;'
    );
});

// Prevent multiple form submissions
let isSubmitting = false;

document.addEventListener('submit', function(e) {
    if (isSubmitting) {
        e.preventDefault();
        return false;
    }
});

// Add loading animation styles
const loadingStyle = document.createElement('style');
loadingStyle.textContent = `
    .btn-login.loading {
        pointer-events: none;
        opacity: 0.8;
    }
    
    .btn-login.success {
        background: linear-gradient(135deg, #4caf50 0%, #45a049 100%) !important;
    }
`;
document.head.appendChild(loadingStyle);