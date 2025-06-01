// Service Management JavaScript

// Load service packages for the dropdown
async function loadServicePackages() {
    try {
        const response = await fetch('/BillingSystem/api/service-packages');
        const packages = await response.json();
        const packageSelect = document.getElementById('servicePackage');
        
        packages.forEach(pkg => {
            const option = document.createElement('option');
            option.value = pkg.id;
            option.textContent = `${pkg.name} - ${pkg.price}`;
            packageSelect.appendChild(option);
        });
    } catch (error) {
        console.error('Error loading service packages:', error);
    }
}

if (document.getElementById('assignServiceForm')) {
    document.getElementById('assignServiceForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const formData = {
            customerPhone: document.getElementById('customerPhone').value,
            servicePackageId: document.getElementById('servicePackage').value,
            startDate: document.getElementById('startDate').value,
            endDate: document.getElementById('endDate').value || null
        };

        try {
            const response = await fetch('/BillingSystem/api/service-subscriptions', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(formData)
            });

            if (response.ok) {
                alert('Service assigned successfully!');
                window.location.href = 'index.html';
            } else {
                const error = await response.json();
                alert('Error: ' + error.message);
            }
        } catch (error) {
            console.error('Error assigning service:', error);
            alert('Error assigning service. Please try again.');
        }
    });

    // Load service packages when the page loads
    loadServicePackages();
} 