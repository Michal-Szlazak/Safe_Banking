
export class TokenDTO {
  // Properties
  public access_token: string;
  public expires_in: string;
  public refresh_token: string;
  public refresh_expires_in: string;

  constructor (access_token: string, expires_in: string, refresh_token: string, refresh_expires_in: string) {

    this.access_token = access_token;
    this.expires_in = expires_in;
    this.refresh_token = refresh_token;
    this.refresh_expires_in = refresh_expires_in;
  }
}
