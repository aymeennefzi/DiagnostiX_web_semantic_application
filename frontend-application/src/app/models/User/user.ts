import { Role } from '../Role/role.enu';

export class User {
  id!: number;
  nom!: string;
  prenom!: string;
  numeroTelephone!: string;
  email!: string;
  passwordResetToken!: string;
  password!: string;
  role!: Role;
  dateNaissance!: Date;
  image!: string;
  verificationCode!: string;
  verified!: boolean;
}
