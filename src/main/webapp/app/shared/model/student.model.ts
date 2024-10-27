import { IStudentDetails } from 'app/shared/model/student-details.model';
import { ISchool } from 'app/shared/model/school.model';
import { IStudClass } from 'app/shared/model/stud-class.model';

export interface IStudent {
  id?: number;
  name?: string | null;
  lastName?: string | null;
  studentDetails?: IStudentDetails | null;
  school?: ISchool | null;
  studClasses?: IStudClass[] | null;
}

export const defaultValue: Readonly<IStudent> = {};
