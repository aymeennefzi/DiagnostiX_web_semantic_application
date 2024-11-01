import { Component, OnInit } from '@angular/core';
import { TipService } from '../../../../../../app/core/services/Tip/tip.service';
import { Tip } from '../../../../../models/Tip/tip.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-tip',
  templateUrl: './tip.component.html',
  styleUrls: ['./tip.component.css']
})
export class TipComponent implements OnInit {
  tips: Tip[] = [];

  constructor(private tipService: TipService) { }

  ngOnInit(): void {
    this.getAllTips();
  }

  getAllTips(): void {
    this.tipService.getAllTips().subscribe(
      (data: Tip[]) => {
        this.tips = data;
      },
      error => {
        console.error('Error fetching tips', error);
      }
    );
  }

  openCreateTipModal(): void {
    Swal.fire({
      title: 'Add a New Tip',
      html: `
        <input id="titre" class="swal2-input" placeholder="Titre">
        <input id="cible" class="swal2-input" placeholder="Cible">
        <textarea id="description" class="swal2-textarea" placeholder="Description"></textarea>
      `,
      focusConfirm: false,
      preConfirm: () => {
        const titre = (document.getElementById('titre') as HTMLInputElement).value;
        const cible = (document.getElementById('cible') as HTMLInputElement).value;
        const description = (document.getElementById('description') as HTMLTextAreaElement).value;

        if (!titre || !cible || !description) {
          Swal.showValidationMessage('Please fill in all fields');
          return false; // Return a value here
        } else {
          return { titre, cible, description }; // Return a value here
        }
      }
    }).then((result) => {
      if (result.isConfirmed) {
        // Handle the confirmed result here
        console.log(result.value);
      }
    });
}
openUpdateTipModal(tip: Tip): void {
  Swal.fire({
    title: 'Update Tip',
    html: `
      <input id="titre" class="swal2-input" value="${tip.titre}" placeholder="Titre">
      <input id="cible" class="swal2-input" value="${tip.cible}" placeholder="Cible" disabled>
      <textarea id="description" class="swal2-textarea" placeholder="Description">${tip.description}</textarea>
    `,
    focusConfirm: false,
    preConfirm: () => {
      const titre = (document.getElementById('titre') as HTMLInputElement).value;
      const description = (document.getElementById('description') as HTMLTextAreaElement).value;

      if (!titre || !description) {
        Swal.showValidationMessage('Please fill in all fields');
        return false; // Return a value here
      } else {
        return { cible: tip.cible, titre, description }; // Return a value here
      }
    }
  }).then((result) => {
    if (result.isConfirmed) {
      this.tipService.updateTip(tip.cible, result.value).subscribe(() => {
        this.getAllTips(); // Refresh the list
        Swal.fire('Success', 'Tip updated successfully', 'success');
      }, (error) => {
        Swal.fire('Error', 'There was a problem updating the tip', 'error');
      });
    }
  });
}

  confirmDeleteTip(cible: string): void {
    Swal.fire({
      title: 'Are you sure?',
      text: 'This action cannot be undone!',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Yes, delete it!'
    }).then((result) => {
      if (result.isConfirmed) {
        this.tipService.deleteTip(cible).subscribe(() => {
          this.getAllTips(); // Refresh the list
          Swal.fire('Deleted!', 'The tip has been deleted.', 'success');
        }, (error) => {
          Swal.fire('Error', 'There was a problem deleting the tip', 'error');
        });
      }
    });
  }
}
