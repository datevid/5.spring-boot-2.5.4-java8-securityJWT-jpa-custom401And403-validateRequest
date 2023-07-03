# spring-boot-2.5.4-java8-securityJWT-jpa-custom401And403-validateRequest
spring-boot-2.5.4-java8-securityJWT-jpa-custom401And403-validateRequest

Características:
- Spring boot 2.5.4
- Java 1.8
- Swagger
- Spring security
- Authentication
- Authorization
- Custom response data for 401 and 403
- Validation of Request params

Credenciales:
```java
@Override
  public void run(String... params) throws Exception {
    AppUser admin = new AppUser();
    admin.setUsername("admin");
    admin.setPassword("admin");
    admin.setEmail("admin@email.com");
    admin.setAppUserRoles(new ArrayList<AppUserRole>(Arrays.asList(AppUserRole.ROLE_ADMIN)));

    userService.signup(admin);

    AppUser client = new AppUser();
    client.setUsername("client");
    client.setPassword("client");
    client.setEmail("client@email.com");
    client.setAppUserRoles(new ArrayList<AppUserRole>(Arrays.asList(AppUserRole.ROLE_CLIENT)));

    userService.signup(client);
  }
```
