// src/app/models/patient.dto.ts
export interface Patient {
    id?: string;         // Optional ID (for existing records)
    nom: string;         // Patient's name
    age: number;         // Patient's age
    sexe: string;        // Patient's gender
    poids?: number;      // Optional weight
    adresse?: string;    // Optional address
    [key: string]: any;  // Additional fields as required
  }
  