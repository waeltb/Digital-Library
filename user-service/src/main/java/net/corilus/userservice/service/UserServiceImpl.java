    package net.corilus.userservice.service;


    import com.azure.storage.blob.BlobContainerClient;
    import com.azure.storage.blob.BlobServiceClient;
    import jakarta.ws.rs.core.Response;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import net.corilus.userservice.dto.AuthenticationRequest;
    import net.corilus.userservice.dto.ImmutableUserDto;
    import net.corilus.userservice.dto.UserDto;
    import net.corilus.userservice.entity.Role;
    import net.corilus.userservice.exception.EmailExistsExecption;
    import net.corilus.userservice.entity.Speciality;
    import net.corilus.userservice.entity.User;
    import net.corilus.userservice.repository.RoleRepository;
    import net.corilus.userservice.repository.SpecialityRepository;
    import net.corilus.userservice.repository.UserRepository;
    import net.corilus.userservice.securityconfig.KeycloakConfig;
    import org.keycloak.admin.client.CreatedResponseUtil;
    import org.keycloak.admin.client.resource.UserResource;
    import org.keycloak.common.util.CollectionUtil;
    import org.keycloak.representations.idm.CredentialRepresentation;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;
    import org.keycloak.representations.idm.UserRepresentation;
    import org.springframework.http.*;

    import org.keycloak.admin.client.Keycloak;
    import org.springframework.web.client.RestTemplate;

    import java.io.ByteArrayInputStream;
    import java.util.*;

    @Service
    @Slf4j
    @RequiredArgsConstructor
    public class UserServiceImpl implements UserService {

    @Autowired
         RoleServiceImpl roleService;
        @Autowired
        private RestTemplate restTemplate;
        @Autowired
        private UserRepository userRepository;
        @Autowired
        SpecialityRepository specialityRepository;
        @Autowired
        RoleRepository roleRepository ;
        @Autowired
        private BlobServiceClient blobServiceClient ;


        @Override
        public String login(AuthenticationRequest authenticationRequest) {

            // Créez les en-têtes de la requête
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            // Créez le corps de la requête
            String body = "grant_type=password&username=" + authenticationRequest.getUsername() + "&password=" + authenticationRequest.getPassword() +
                    "&client_id=login-app&client_secret=XeZM5aBXDTrdn6eqeR0TUhZRSTlibOF1";
            // Créez l'objet HttpEntity avec les en-têtes et le corps
            HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
            // Envoyez la requête POST
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    "http://localhost:8080/realms/corilus/protocol/openid-connect/token",
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            // Récupérez la réponse
            String responseBody = responseEntity.getBody();

            return responseBody;
        }

        @Override
        public String createUser(UserDto userDto) {
             String user ="user";
            try {
                UserRepresentation userRep= mapUserRep(userDto);
                Keycloak keycloak = KeycloakConfig.getInstance();
                List<UserRepresentation> usernameRepresentations = keycloak.realm("corilus").users().searchByUsername(userDto.username(),true);
                List<UserRepresentation> emailRepresentations = keycloak.realm("corilus").users().searchByEmail(userDto.email(),true);

                if(!(usernameRepresentations.isEmpty() && emailRepresentations.isEmpty())){
                    throw new EmailExistsExecption("username or email already exists");
                }
                Response response = keycloak.realm("corilus").users().create(userRep);


                if (response.getStatus() != 201) {
                    throw new RuntimeException("Failed to create user");
                }
                String userId = CreatedResponseUtil.getCreatedId(response);
                roleService.getRole(user);
                roleService.assignRole(userId,user);
                UserResource userResource = keycloak.realm("corilus").users().get(userId);
                userResource.sendVerifyEmail();
             // Create directory with the username in Azure Blob Storage
                BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient("useraccount");
                String directoryName = userDto.username() ;
              containerClient.getBlobClient(directoryName);
                //containerClient.getBlobClient(directoryName).upload(new ByteArrayInputStream(new byte[0]), 0);

                return "User created";

            }
            catch (RuntimeException e){
                throw new RuntimeException(e.getMessage());
            }

        }


        @Override
        public String createExpert(UserDto userDto,String specialityName) {
            String user ="expert";
            try {
                UserRepresentation userRep= mapUserRep(userDto);
                Keycloak keycloak = KeycloakConfig.getInstance();
                List<UserRepresentation> usernameRepresentations = keycloak.realm("corilus").users().searchByUsername(userDto.username(),true);
                List<UserRepresentation> emailRepresentations = keycloak.realm("corilus").users().searchByEmail(userDto.email(),true);

                if(!(usernameRepresentations.isEmpty() && emailRepresentations.isEmpty())){
                    throw new EmailExistsExecption("username or email already exists");
                }
                Speciality speciality = specialityRepository.findByName(specialityName);
                if (speciality != null) {


                } else {
                    // Vous pouvez lancer une exception si la spécialité n'existe pas
                }
                Response response = keycloak.realm("corilus").users().create(userRep);


                if (response.getStatus() != 201) {
                    throw new RuntimeException("Failed to create Expert");
                }
                String userId = CreatedResponseUtil.getCreatedId(response);
                roleService.getRole(user);
                roleService.assignRole(userId,user);
                UserResource userResource = keycloak.realm("corilus").users().get(userId);
                userResource.sendVerifyEmail();
                User userEntity = convertToEntity(userDto);
                userEntity.setSpeciality(speciality);
                userRepository.save(userEntity);
                return "Expert created";

            }
            catch (RuntimeException e){
                throw new RuntimeException(e.getMessage());
            }
        }

        @Override
        public void updateUser(String id, UserDto user) {
            UserRepresentation userRep = mapUserRep(user);
            Keycloak k = KeycloakConfig.getInstance();
            k.realm("corilus").users().get(id).update(userRep);

        }

        @Override
        public List<UserDto> getUsers() {
            Keycloak k = KeycloakConfig.getInstance();
            List<UserRepresentation> userRepresentations=k.realm("corilus").users().list();
            return mapUsers(userRepresentations);
        }

        @Override
        public UserDto getUser(String id) {
            Keycloak k = KeycloakConfig.getInstance();
            return mapUser(k.realm("pfe").users().get(id).toRepresentation());
        }

        @Override
        public void deleteUser(String id) {
            Keycloak k = KeycloakConfig.getInstance();
            k.realm("pfe").users().delete(id);
        }
        @Override
        public UserRepresentation mapUserRep(UserDto userDto){

                UserRepresentation userRep = new UserRepresentation();
                userRep.setUsername(userDto.username());

                userRep.setLastName(userDto.lastName());
                userRep.setEmail(userDto.email());
                userRep.setEnabled(true);
                userRep.setEmailVerified(false);

            Map<String, List<String>> attributes = new HashMap<>();
            if (userDto.mobileNumber() != null) {
                List<String> mobileNumber = new ArrayList<>();
                mobileNumber.add(userDto.mobileNumber());
                attributes.put("mobileNumber", mobileNumber);
            }
            userRep.setAttributes(attributes);

                List<CredentialRepresentation> creds = new ArrayList<>();
                CredentialRepresentation cred = new CredentialRepresentation();
                cred.setTemporary(false);
                cred.setValue(userDto.password());
                creds.add(cred);
                userRep.setCredentials(creds);
                return userRep ;

        }
        @Override
        public List<UserDto> mapUsers(List<UserRepresentation> userRepresentations){
            List<UserDto> users= new ArrayList<>();
            if (CollectionUtil.isNotEmpty(userRepresentations)){
                userRepresentations.forEach(userRep->{
                    users.add(mapUser(userRep));
                });
            }
            return users;
        }
        @Override
        public UserDto mapUser(UserRepresentation userRep){
          return ImmutableUserDto.builder()
                  .lastName(userRep.getLastName())
                  .email(userRep.getEmail())
                  .username(userRep.getUsername())
                  .build();

        }
        @Override
        public void forgotPassword(String email) {
        Keycloak keycloak = KeycloakConfig.getInstance();
        List<UserRepresentation> emailRepresentations = keycloak.realm("corilus").users().searchByEmail(email,true);
        System.out.println("********test0******** "+emailRepresentations);
        if (!emailRepresentations.isEmpty()) {
            UserRepresentation userRepresentation = emailRepresentations.get(0); // Get the first user
            System.out.println("********test1******** "+userRepresentation);

            try {
                UserResource userResource = keycloak.realm("corilus").users().get(userRepresentation.getId());
                List<String> actions = new ArrayList<>();
                actions.add("UPDATE_PASSWORD");
                userResource.executeActionsEmail(actions);

                System.out.println("Password reset email sent successfully.");
            } catch (Exception e) {
                System.err.println("Error resetting password: " + e.getMessage());
                throw new RuntimeException("Failed to reset password.");
            }
        } else {
            System.err.println("User with username '" + email + "' not found.");
            throw new RuntimeException("User not found.");
        }
    }

        @Override
        public List<User> getAvailableExperts(String specialityName) {
            Date currentDate = new Date();
            return userRepository.findByRole_NameAndAvailabilityDateLessThanEqualAndSpeciality_NameOrderByAvailabilityDate("expert", currentDate,specialityName);
        }


        private User convertToEntity(UserDto userDto) {
            Role role  = roleRepository.findByName("expert");
            return User.builder()
                    .lastName(userDto.lastName())
                    .email(userDto.email())
                    .username(userDto.username())
                    .password(userDto.password())
                    .mobileNumber(userDto.mobileNumber())
                    .availabilityDate(userDto.availabilityDate().orElse(new Date()))
                    .role(role)
                    .build();
        }



    }

