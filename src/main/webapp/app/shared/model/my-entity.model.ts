export interface IMyEntity {
  id?: number;
  name?: string;
  description?: string | null;
}

export const defaultValue: Readonly<IMyEntity> = {};
