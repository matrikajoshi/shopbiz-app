export class User {
  roles: string[];

  constructor(
    public email: string,
    public id: string,
    private _token: string,
    roles: string[],
    private _tokenExpirationDate: Date
  ) {
    this.roles = roles;
  }

  get token() {
    if (!this._tokenExpirationDate || new Date() > this._tokenExpirationDate) {
      return null;
    }
    return this._token;
  }
}
