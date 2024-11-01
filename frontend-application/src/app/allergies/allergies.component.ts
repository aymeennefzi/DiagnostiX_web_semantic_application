import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-allergies',
  templateUrl: './allergies.component.html',
  styleUrls: ['./allergies.component.css']
})
export class AllergiesComponent implements OnInit {
  allergies: any[] = [
    { name: 'Pollen Allergy', severityLevel: 'High', lastOccurrence: '2024-09-15' },
    { name: 'Dust Allergy', severityLevel: 'Medium', lastOccurrence: '2024-08-20' },
    { name: 'Pet Allergy', severityLevel: 'Low', lastOccurrence: '2024-10-10' },
    { name: 'Food Allergy', severityLevel: 'Severe', lastOccurrence: '2024-07-22' }
  ];
  allergyForm: FormGroup;
  selectedAllergy: any = null;
  isEditMode: boolean = false;

  constructor(private fb: FormBuilder) {
    // Initialize the form with validators
    this.allergyForm = this.fb.group({
      name: ['', Validators.required],
      severityLevel: ['', Validators.required],
      lastOccurrence: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.getAllergies();
  }

  // Fetch all allergies (from local array)
  getAllergies() {
    console.log("Fetched allergies:", this.allergies);
  }

  // Add a new allergy (to local array)
  addAllergy() {
    if (this.allergyForm.valid) {
      const newAllergy = { ...this.allergyForm.value };
      this.allergies.push(newAllergy);
      console.log("Added allergy:", newAllergy);

      this.allergyForm.reset();
    }
  }

  // Set the allergy to edit mode
  editAllergy(allergy: any) {
    this.isEditMode = true;
    this.selectedAllergy = allergy;
    this.allergyForm.patchValue(allergy);
  }

  // Update an allergy (in local array)
  updateAllergy() {
    if (this.allergyForm.valid && this.selectedAllergy) {
      const index = this.allergies.findIndex(a => a.name === this.selectedAllergy.name);
      if (index !== -1) {
        this.allergies[index] = { ...this.allergyForm.value };
        console.log("Updated allergy:", this.allergies[index]);

        this.cancelEdit();
      }
    }
  }

  // Delete an allergy (from local array)
  deleteAllergy(name: string) {
    this.allergies = this.allergies.filter(allergy => allergy.name !== name);
    console.log("Deleted allergy with name:", name);
  }

  // Cancel edit mode and reset form
  cancelEdit() {
    this.isEditMode = false;
    this.selectedAllergy = null;
    this.allergyForm.reset();
  }
}
