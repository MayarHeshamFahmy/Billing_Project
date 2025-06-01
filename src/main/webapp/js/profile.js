// Profile Management JavaScript

if (document.getElementById('createProfileForm')) {
    document.getElementById('createProfileForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const formData = {
            name: document.getElementById('name').value,
            description: document.getElementById('description').value,
            basePrice: parseFloat(document.getElementById('basePrice').value)
        };

        try {
            const response = await fetch('/BillingSystem/api/profiles', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(formData)
            });

            if (response.ok) {
                alert('Profile created successfully!');
                window.location.href = 'index.html';
            } else {
                const error = await response.json();
                alert('Error: ' + error.message);
            }
        } catch (error) {
            console.error('Error creating profile:', error);
            alert('Error creating profile. Please try again.');
        }
    });
} 