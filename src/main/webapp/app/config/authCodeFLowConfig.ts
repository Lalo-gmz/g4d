import { AuthConfig } from 'angular-oauth2-oidc';

export const authCodeFlowConfig: AuthConfig = {
  // Url of the Identity Provider
  issuer: 'https://gitlab.com',

  // URL of the SPA to redirect the user to after login
  redirectUri: 'http://localhost:9000/login',

  // The SPA's id. The SPA is registerd with this id at the auth-server
  // clientId: 'server.code',
  clientId: '0a8b42db317560d8fdd46d71c0a0901e2624e32e8ffc649fda2cb5fcb8e4d5be',

  // Just needed if your auth server demands a secret. In general, this
  // is a sign that the auth server is not configured with SPAs in mind
  // and it might not enforce further best practices vital for security
  // such applications.
  dummyClientSecret: '2161e7d6d19e542f5aec6399be06976dc08aed305242a48797d5a0f59253f860',

  responseType: 'code',

  // set the scope for the permissions the client should request
  // The first four are defined by OIDC.
  // Important: Request offline_access to get a refresh token
  // The api scope is a usecase specific one
  // scope: 'read_user',
  scope: 'email read_user api',

  showDebugInformation: true,
};
