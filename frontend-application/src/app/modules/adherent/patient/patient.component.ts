import { Component, OnInit } from '@angular/core';
import { PatientService } from '../../../services/patient.service';
import { ChangeDetectorRef } from '@angular/core';


@Component({
  selector: 'app-patient',
  templateUrl: './patient.component.html',
  styleUrls: ['./patient.component.css']
})
export class PatientComponent implements OnInit {
  patients: any[] = [];
  patient: any = {
    nom: '',
    age: '',
    sexe: '',
    poids: '',
    antecedentsMedicaux: '',
    niveauDouleur: '',
    dateDiagnostic:'',
    historiqueMedical:''
  };

  selectedPatient: any;
  showDetailsModal: boolean = false;
  showAddModal: boolean = false; // Flag for Add Patient modal
  showUpdateModal: boolean = false; // Flag for Update Patient modal

  constructor(private patientService: PatientService, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.getAllPatients();
  }

  getAllPatients(): void {
    this.patientService.getAllPatients().subscribe((data) => {
      this.patients = data;
    });
  }

  // Open the modal to add a new patient
  openAddModal(): void {
    this.resetPatientForm();
    this.showAddModal = true;
  }

  // Open the modal to update a patient
  openUpdateModal(nom: string): void {
    this.patientService.getPatientInfo(nom).subscribe((data) => {
      this.patient = data;
      this.showUpdateModal = true; // Show the update modal
    });
  }
  historiqueMedicalInput: string = '';
  antecedentsMedicauxInput: string = '';

  updateHistoriqueMedical() {
    this.patient.historiqueMedical = this.historiqueMedicalInput.split(',').map(item => item.trim());
  }

  updateAntecedentsMedicaux() {
    this.patient.antecedentsMedicaux = this.antecedentsMedicauxInput.split(',').map(item => item.trim());
  }
  // Add a new patient
// In your PatientComponent
addPatient(): void {
  this.patientService.addPatient(this.patient).subscribe({
    next: () => {
      this.getAllPatients();
      this.closeAddModal(); // Close the modal after adding
    },
    error: (err) => {
      console.error('Error adding patient:', err);
      alert('Failed to add patient: ' + err.error.message); // Display error message to the user
    }
  });
}


  // Save the updated patient information
  saveUpdatedPatient(): void {
    this.patientService.updatePatient(this.patient.nom, this.patient).subscribe(() => {
      this.getAllPatients();
      this.closeUpdateModal(); // Close the modal after saving
    });
  }

  // Close the Add Patient modal
  closeAddModal(): void {
    this.showAddModal = false;
    this.resetPatientForm();
  }

  // Close the Update Patient modal
  closeUpdateModal(): void {
    this.showUpdateModal = false;
    this.resetPatientForm();
  }

  // Reset the patient form to initial state
  private resetPatientForm(): void {
    this.patient = {
      nom: '',
      age: '',
      sexe: '',
      poids: '',
      antecedentsMedicaux: '',
      niveauDouleur: ''
    };
  }

  // // Show details of a selected patient
  // getPatientInfo(nom: string): void {
  //   this.patientService.getPatientInfo(nom).subscribe((data) => {
  //     console.log('Fetched patient data:', data); // Log the fetched data
  //     this.selectedPatient = data;
  //     this.showDetailsModal = true;
  //   }, (error) => {
  //     console.error('Error fetching patient info:', error); // Log any errors
  //   });
  // }
  
  // Show details of a selected patient
getPatientInfo(nom: string): void {
  this.patientService.getPatientInfo(nom).subscribe((data) => {
    console.log('Fetched patient data:', data); // Log the fetched data

    // Assuming data.results is the array you want to map to selectedPatient
    if (data.results && data.results.length > 0) {
      this.selectedPatient = {};
      data.results.forEach((item: { property: string; value: string | number }) => {
        this.selectedPatient[item.property] = item.value; // Map the properties to selectedPatient
      });
    }

    this.showDetailsModal = true; // Show the modal
    this.cdr.detectChanges(); // Trigger change detection
  }, (error) => {
    console.error('Error fetching patient info:', error); // Log any errors
  });
}


  // Close the details modal
  closeDetailsModal(): void {
    this.showDetailsModal = false;
    this.selectedPatient = null;
  }

  // Delete a patient
  deletePatient(nom: string): void {
    this.patientService.deletePatient(nom).subscribe(() => {
      this.getAllPatients();
    });
  }
}
