export interface ISchool {
  id?: number;
  name?: string | null;
  address?: string | null;
}

export const defaultValue: Readonly<ISchool> = {};
