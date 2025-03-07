package com.challenge.api.config;

//import dependencies
//this is what is not working for me, I have spent a lot of time troubleshooting and can not figure out 
//why they won't import properly

/*
 * API Security 
 * 1. User Login & Authentication:
 *    - Make users log in with username and password
 *    - Use library to store passwords securely
 *    - Give users a token when they log in
 *    - Make them include this token with future requests
 * 
 * 2. User Permissions:
 *    - Regular users can only view data
 *    - Admin users can create/edit/delete data
 *    - Block users from accessing other users' private data
 * 
 * 3. Basic Protection:
 *    - Use HTTPS (secure connection)
 *    - Check that data isn't too large
 *    - Block too many requests from same user
 *    - Basic Logging
 * 
 */

//@EnableWebSecurity
public class SecConfig /*extends WebSecurityConfigurerAdapter*/{
}
