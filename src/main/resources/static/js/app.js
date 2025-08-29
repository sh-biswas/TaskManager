// Task Manager JavaScript

document.addEventListener('DOMContentLoaded', function() {
    
    // Auto-hide alerts after 5 seconds
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        }, 5000);
    });
    
    // Add data attributes to task items for CSS styling
    const taskItems = document.querySelectorAll('.task-item');
    taskItems.forEach(item => {
        const priorityBadge = item.querySelector('.badge:last-child');
        const statusBadge = item.querySelector('.badge:first-child');
        
        if (priorityBadge) {
            const priorityText = priorityBadge.textContent.trim();
            if (priorityText === 'Urgent') item.setAttribute('data-priority', 'URGENT');
            else if (priorityText === 'High') item.setAttribute('data-priority', 'HIGH');
            else if (priorityText === 'Medium') item.setAttribute('data-priority', 'MEDIUM');
            else if (priorityText === 'Low') item.setAttribute('data-priority', 'LOW');
        }
        
        if (statusBadge) {
            const statusText = statusBadge.textContent.trim();
            if (statusText === 'Completed') item.setAttribute('data-status', 'COMPLETED');
            else if (statusText === 'In Progress') item.setAttribute('data-status', 'IN_PROGRESS');
            else if (statusText === 'Cancelled') item.setAttribute('data-status', 'CANCELLED');
            else item.setAttribute('data-status', 'PENDING');
        }
    });
    
    // Form validation enhancement
    const taskForm = document.querySelector('form[th\\:action*="/tasks"]');
    if (taskForm) {
        taskForm.addEventListener('submit', function(e) {
            const titleInput = this.querySelector('#title');
            if (titleInput && titleInput.value.trim() === '') {
                e.preventDefault();
                titleInput.focus();
                showToast('Please enter a task title', 'error');
            }
        });
    }
    
    // Enhanced delete confirmation
    const deleteButtons = document.querySelectorAll('form[th\\:action*="/delete"] button[type="submit"]');
    deleteButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            if (!confirm('Are you sure you want to delete this task? This action cannot be undone.')) {
                e.preventDefault();
            }
        });
    });
    
    // Auto-focus on title input when modal opens
    const editModals = document.querySelectorAll('.modal');
    editModals.forEach(modal => {
        modal.addEventListener('shown.bs.modal', function() {
            const titleInput = this.querySelector('input[name="title"]');
            if (titleInput) {
                titleInput.focus();
            }
        });
    });
    
    // Keyboard shortcuts
    document.addEventListener('keydown', function(e) {
        // Ctrl/Cmd + N to focus on new task title
        if ((e.ctrlKey || e.metaKey) && e.key === 'n') {
            e.preventDefault();
            const titleInput = document.querySelector('#title');
            if (titleInput) {
                titleInput.focus();
            }
        }
        
        // Escape to close modals
        if (e.key === 'Escape') {
            const openModal = document.querySelector('.modal.show');
            if (openModal) {
                const modal = bootstrap.Modal.getInstance(openModal);
                if (modal) {
                    modal.hide();
                }
            }
        }
    });
    
    // Search functionality (if needed in future)
    const searchInput = document.querySelector('#search');
    if (searchInput) {
        searchInput.addEventListener('input', function() {
            const searchTerm = this.value.toLowerCase();
            const taskItems = document.querySelectorAll('.task-item');
            
            taskItems.forEach(item => {
                const title = item.querySelector('h6').textContent.toLowerCase();
                const description = item.querySelector('p').textContent.toLowerCase();
                
                if (title.includes(searchTerm) || description.includes(searchTerm)) {
                    item.style.display = 'block';
                } else {
                    item.style.display = 'none';
                }
            });
        });
    }
    
    // Toast notification function
    function showToast(message, type = 'info') {
        const toastContainer = document.getElementById('toast-container') || createToastContainer();
        
        const toast = document.createElement('div');
        toast.className = `toast align-items-center text-white bg-${type === 'error' ? 'danger' : type === 'success' ? 'success' : 'info'} border-0`;
        toast.setAttribute('role', 'alert');
        toast.setAttribute('aria-live', 'assertive');
        toast.setAttribute('aria-atomic', 'true');
        
        toast.innerHTML = `
            <div class="d-flex">
                <div class="toast-body">
                    ${message}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
        `;
        
        toastContainer.appendChild(toast);
        
        const bsToast = new bootstrap.Toast(toast);
        bsToast.show();
        
        // Remove toast element after it's hidden
        toast.addEventListener('hidden.bs.toast', function() {
            toast.remove();
        });
    }
    
    function createToastContainer() {
        const container = document.createElement('div');
        container.id = 'toast-container';
        container.className = 'toast-container position-fixed top-0 end-0 p-3';
        container.style.zIndex = '9999';
        document.body.appendChild(container);
        return container;
    }
    
    // Add loading states to buttons
    const submitButtons = document.querySelectorAll('button[type="submit"]');
    submitButtons.forEach(button => {
        button.addEventListener('click', function() {
            if (!this.disabled) {
                const originalText = this.innerHTML;
                this.innerHTML = '<span class="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>Processing...';
                this.disabled = true;
                
                // Re-enable after a delay (in case of validation errors)
                setTimeout(() => {
                    this.innerHTML = originalText;
                    this.disabled = false;
                }, 3000);
            }
        });
    });
    
    // Auto-save draft functionality
    const titleInput = document.querySelector('#title');
    const descriptionInput = document.querySelector('#description');
    
    if (titleInput && descriptionInput) {
        const saveDraft = () => {
            const draft = {
                title: titleInput.value,
                description: descriptionInput.value,
                timestamp: Date.now()
            };
            localStorage.setItem('taskDraft', JSON.stringify(draft));
        };
        
        // Save draft on input
        titleInput.addEventListener('input', saveDraft);
        descriptionInput.addEventListener('input', saveDraft);
        
        // Load draft on page load
        const savedDraft = localStorage.getItem('taskDraft');
        if (savedDraft) {
            const draft = JSON.parse(savedDraft);
            const oneHourAgo = Date.now() - (60 * 60 * 1000);
            
            if (draft.timestamp > oneHourAgo) {
                titleInput.value = draft.title || '';
                descriptionInput.value = draft.description || '';
            } else {
                localStorage.removeItem('taskDraft');
            }
        }
        
        // Clear draft on successful form submission
        const form = titleInput.closest('form');
        if (form) {
            form.addEventListener('submit', function() {
                localStorage.removeItem('taskDraft');
            });
        }
    }
    
    // Responsive table adjustments
    function adjustTableLayout() {
        const taskItems = document.querySelectorAll('.task-item');
        taskItems.forEach(item => {
            const row = item.querySelector('.row');
            if (window.innerWidth < 768 && row) {
                row.style.flexDirection = 'column';
            }
        });
    }
    
    window.addEventListener('resize', adjustTableLayout);
    adjustTableLayout();
    
    console.log('Task Manager initialized successfully!');
});
