
export class ApiErrorResponse {
  status: string;
  message: string;
  errors: string[];

  constructor(status: string, message: string, errors: string[] = []) {
    this.status = status;
    this.message = message;
    this.errors = errors;
  }
}

export interface SuccessResponse {
  status: number;
  message: string;
}
