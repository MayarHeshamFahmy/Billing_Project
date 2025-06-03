// Service Management JavaScript

// Load service packages for the dropdown
async function loadServicePackages() {
  console.log('loadServicePackages function started');
  try {
    console.log('Making fetch request to /BillingSystem/api/service-packages');
    const response = await fetch('/BillingSystem/api/service-packages');
    console.log('Response received:', response);
    
    const data = await response.json();
    console.log('Response data:', data);
    
    if (!data.packages || !Array.isArray(data.packages)) {
      console.error('Invalid data structure received:', data);
      return;
    }

    const packageSelect = document.getElementById('servicePackage');
    if (!packageSelect) {
      console.error('Service package select element not found');
      return;
    }

    // Clear existing options
    packageSelect.innerHTML = '';
    
    // Add default option
    const defaultOption = document.createElement('option');
    defaultOption.value = '';
    defaultOption.textContent = 'Select a service package';
    packageSelect.appendChild(defaultOption);

    // Add package options
    data.packages.forEach(pkg => {
      const option = document.createElement('option');
      option.value = pkg.id;
      option.textContent = `${pkg.name} - $${pkg.price}`;
      packageSelect.appendChild(option);
    });
    console.log('Service packages loaded successfully');
  } catch (error) {
    console.error('Error in loadServicePackages:', error);
  }
}

// Add logging to check if the form exists and event listener is attached
if (document.getElementById('assignServiceForm')) {
    console.log('Assign service form found, attaching event listener');
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
    console.log('Calling loadServicePackages');
    loadServicePackages();
} else {
    console.log('Assign service form not found');
} 