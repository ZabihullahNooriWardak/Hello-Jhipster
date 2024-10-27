export interface IStudentDetails {
  id?: number;
  phone?: string | null;
  email?: string | null;
  address?: string | null;
}

export const defaultValue: Readonly<IStudentDetails> = {};
