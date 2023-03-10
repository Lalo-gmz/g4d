import { Component, ViewChild, OnInit, AfterViewInit, ElementRef } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { LoginService } from 'app/login/login.service';
import { AccountService } from 'app/core/auth/account.service';
import { JwksValidationHandler, OAuthService } from 'angular-oauth2-oidc';

import { authCodeFlowConfig } from 'app/config/authCodeFLowConfig';

@Component({
  selector: 'jhi-login',
  templateUrl: './login.component.html',
})
export class LoginComponent implements OnInit, AfterViewInit {
  @ViewChild('username', { static: false })
  username!: ElementRef;

  authenticationError = false;

  loginForm = new FormGroup({
    username: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    password: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    rememberMe: new FormControl(false, { nonNullable: true, validators: [Validators.required] }),
  });

  constructor(
    private oauthService: OAuthService,
    private accountService: AccountService,
    private loginService: LoginService,
    private router: Router
  ) {
    this.configureSingleSignOn();
  }

  ngOnInit(): void {
    // if already authenticated then navigate to home page
    this.accountService.identity().subscribe(() => {
      if (this.accountService.isAuthenticated()) {
        this.router.navigate(['']);
      }
    });
  }

  configureSingleSignOn() {
    this.oauthService.configure(authCodeFlowConfig);
    this.oauthService.tokenValidationHandler = new JwksValidationHandler();
    this.oauthService.loadDiscoveryDocumentAndTryLogin();
  }

  ngAfterViewInit(): void {
    this.username.nativeElement.focus();
  }

  loginWithGitLab() {
    this.oauthService.initImplicitFlow();
  }

  logOut() {
    this.oauthService.logOut();
  }

  get token() {
    let claims: any = this.oauthService.getIdentityClaims();
    let token: any = this.oauthService.getAccessToken();
    console.log(token);
    return claims ? claims : null;
  }

  obtenerInfo() {
    this.oauthService.loadUserProfile().then(userInfo => {
      console.log('mail: ', userInfo);
    });
  }

  login(): void {
    this.loginService.login(this.loginForm.getRawValue()).subscribe({
      next: () => {
        this.authenticationError = false;
        if (!this.router.getCurrentNavigation()) {
          // There were no routing during login (eg from navigationToStoredUrl)
          this.router.navigate(['']);
        }
      },
      error: () => (this.authenticationError = true),
    });
  }
}
