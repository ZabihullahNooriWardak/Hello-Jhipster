import { IStudent } from 'app/shared/model/student.model';

export interface IStudClass {
  id?: number;
  className?: string | null;
  subject?: string | null;
  students?: IStudent[] | null;
}

export const defaultValue: Readonly<IStudClass> = {};
