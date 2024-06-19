import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {RegisterDTO} from "../dtos/user/register.dto";
import {LoginDTO} from "../dtos/user/login.dto";
import {environment} from "../environments/environment";
import {HttpUtilService} from "./http.util.service";
import {UserResponse} from "../responses/user/user.response";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiRegister = `${environment.apiBaseUrl}/users/register`;
  private apiLogin = environment.apiBaseUrl + "/users/login";
  private apiUserDetail = `${environment.apiBaseUrl}/users/details`;
  private apiConfig = {
    headers: this.httpUtilService.createHeaders(),
  }

  constructor(
    private http: HttpClient,
    private httpUtilService: HttpUtilService) {
  }


  register(registerDTO: RegisterDTO): Observable<any> {
    return this.http.post(this.apiRegister, registerDTO, this.apiConfig);
  }

  login(loginDTO: LoginDTO): Observable<any> {
    return this.http.post(this.apiLogin, loginDTO, this.apiConfig);
  }

  getUserDetail(token: string) {
    return this.http.post(this.apiUserDetail,
      {
        headers: new HttpHeaders({
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`
        })
      });
  }

  saveUserResponseToLocalStorage(userResponse?: UserResponse) {
    try {
      if (userResponse == null || !userResponse) {
        return;
      }
      // Convert the userResponse object to a JSON string
      const userResponseJSON = JSON.stringify(userResponse);
      // Save the JSON string to local storage with a key
      localStorage.setItem("user", userResponseJSON);
      console.log('User response saved to local storage.');
    } catch (error) {
      console.error("Error saving user response to local storage", error);
    }
  }

  getUserResponseFromLocalStorage(): UserResponse | null {
    try {
      const userResponseJSON = localStorage.getItem("user");
      if (userResponseJSON == null) {
        return null;
      }
      return JSON.parse(userResponseJSON!);
    } catch (error) {
      console.error("Error retrieving user response to local storage", error);
      return null;
    }
  }

  removeUserResponseFromLocalStorage() {
    try {
      localStorage.removeItem("user");
    } catch (error) {
      console.error("Error removing user response to local storage", error);
    }
  }
}
