    package net.corilus.userservice.service;


    import com.azure.storage.blob.BlobClient;
    import com.azure.storage.blob.BlobContainerClient;
    import com.azure.storage.blob.BlobServiceClient;
    import jakarta.ws.rs.core.Response;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import net.corilus.userservice.dto.AuthenticationRequest;
    import net.corilus.userservice.dto.Container;
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
    import org.keycloak.admin.client.resource.RealmResource;
    import org.keycloak.admin.client.resource.UserResource;
    import org.keycloak.admin.client.resource.UsersResource;
    import org.keycloak.common.util.CollectionUtil;
    import org.keycloak.representations.idm.CredentialRepresentation;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.core.io.InputStreamResource;
    import org.springframework.core.io.Resource;
    import org.springframework.stereotype.Service;
    import org.keycloak.representations.idm.UserRepresentation;
    import org.springframework.http.*;

    import org.keycloak.admin.client.Keycloak;
    import org.springframework.web.client.RestTemplate;
    import org.springframework.web.multipart.MultipartFile;

    import java.io.ByteArrayInputStream;
    import java.io.ByteArrayOutputStream;
    import java.io.IOException;
    import java.io.InputStream;
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
        @Autowired
        AzureStorageServiceImpl azureStorageService;
        @Value("${azure.storage.connection.string}")
        private String connectionString;

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

                User userEntity = convertUserToEntity(userDto);
                userRepository.save(userEntity);




                if (response.getStatus() != 201) {
                    throw new RuntimeException("Failed to create user");
                }
                String userId = CreatedResponseUtil.getCreatedId(response);
                System.out.println("userID est de createUser"+userId);
                roleService.getRole(user);
                roleService.assignRole(userId,user);
                UserResource userResource = keycloak.realm("corilus").users().get(userId);
                userResource.sendVerifyEmail();


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
                User userEntity = convertExpertToEntity(userDto);
                userEntity.setSpeciality(speciality);
                userRepository.save(userEntity);
                return "Expert created";

            }
            catch (RuntimeException e){
                throw new RuntimeException(e.getMessage());
            }
        }

        @Override
        public void updateUser( UserDto userDto,String userId) {
            Keycloak keycloak = KeycloakConfig.getInstance();

            // Accès à la ressource Realm
            RealmResource realmResource = keycloak.realm("corilus");
            UserRepresentation user = realmResource.users().get(userId).toRepresentation();
            String userUsername =user.getUsername();
            System.out.println("hser de bd est "+userUsername);
            // Accès à la ressource Users
            UsersResource usersResource = realmResource.users();

            UserResource userResource = usersResource.get(userId);
            UserRepresentation userRepresentation = userResource.toRepresentation();


            // Modifier les champs nécessaires

            userRepresentation.setFirstName(userDto.firstName());
            userRepresentation.setLastName(userDto.lastName());
            userRepresentation.setEmail(userDto.email());

            Map<String, List<String>> attributes = new HashMap<>();
            if (userDto.mobileNumber() != null) {
                List<String> mobileNumber = new ArrayList<>();
                mobileNumber.add(userDto.mobileNumber());
                attributes.put("mobileNumber", mobileNumber);
            }
            userRepresentation.setAttributes(attributes);
            if (userDto.country() != null) {
                List<String> country = new ArrayList<>();
                country.add(userDto.country().get().name());
                attributes.put("country", country);
            }
            userRepresentation.setAttributes(attributes);
            // Mettre à jour l'utilisateur
            userResource.update(userRepresentation);
          User user1 =  userRepository.findByUsername(userUsername);
          user1.setFirstName(userDto.firstName());
          user1.setLastName(userDto.lastName());
          user1.setEmail(userDto.email());
          user1.setMobileNumber(userDto.mobileNumber());
          user1.setCountry(userDto.country().get());
          userRepository.save(user1);

        }

        @Override
        public List<UserDto> getUsers() {

            List<User> users= userRepository.findAll();
            List<UserDto> usersDTO= new ArrayList<>();
            if (CollectionUtil.isNotEmpty(users)){
                users.forEach(user->{
                    usersDTO.add(mapUserEntityToDto(user));
                });
            }
            return usersDTO;
        }

        @Override
        public UserDto getUser(String username) {
            /*
            Keycloak k = KeycloakConfig.getInstance();
            return mapUser(k.realm("corilus").users().get(id).toRepresentation());

             */
            User user = userRepository.findByUsername(username);
            return mapUserEntityToDto(user);
        }

        @Override
        public void deleteUser(String id) {
            Keycloak k = KeycloakConfig.getInstance();
            k.realm("corilus").users().delete(id);
        }

        @Override
        public UserRepresentation mapUserRep(UserDto userDto) {
            UserRepresentation userRep = new UserRepresentation();
            userRep.setFirstName(userDto.firstName());
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

            // Set credentials
            if (userDto.password() != null) {
                List<CredentialRepresentation> creds = new ArrayList<>();
                CredentialRepresentation cred = new CredentialRepresentation();
                cred.setTemporary(false);
                cred.setType(CredentialRepresentation.PASSWORD);
                cred.setValue(userDto.password().orElse(null));
                creds.add(cred);
                userRep.setCredentials(creds);
            }

            log.info("UserRepresentation: {}", userRep);
            return userRep;
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
            String mobileNumber = null;

            // Check if the userRep has the "mobileNumber" attribute
            if (userRep.getAttributes() != null && userRep.getAttributes().containsKey("mobileNumber")) {
                // Get the list of mobile numbers
                List<String> mobileNumbers = userRep.getAttributes().get("mobileNumber");
                if (mobileNumbers != null && !mobileNumbers.isEmpty()) {
                    // Use the first mobile number in the list
                    mobileNumber = mobileNumbers.get(0);
                }
            }
          return ImmutableUserDto.builder()
                  .firstName(userRep.getFirstName())
                  .lastName(userRep.getLastName())
                  .email(userRep.getEmail())
                  .username(userRep.getUsername())
                  .mobileNumber(mobileNumber)
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

        @Override
        public void uploadImage(MultipartFile file, String username) throws IOException {
            String fileName = username + ".png";

            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient("wael");
            BlobClient blobClient = containerClient.getBlobClient(fileName);

            blobClient.upload(file.getInputStream(), file.getSize(), true);
        }

        @Override
        public ResponseEntity<Resource> getImage(String username) {
            String fileName = username + ".png";
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient("wael");
            BlobClient blobClient = containerClient.getBlobClient(fileName);

            // Download the blob to an InputStream
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            blobClient.download(outputStream);
            byte[] imageBytes = outputStream.toByteArray();

            InputStream inputStream = new ByteArrayInputStream(imageBytes);
            InputStreamResource resource = new InputStreamResource(inputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "image/png"); // or the appropriate mime type
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");

            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        }

        @Override
        public UserDto getUserById(Long id) {
            User user = userRepository.findById(id).orElse(null);
            return mapUserEntityToDto(user);
        }

        private User convertExpertToEntity(UserDto userDto) {
            Role role  = roleRepository.findByName("expert");
            return User.builder()
                    .firstName(userDto.firstName())
                    .lastName(userDto.lastName())
                    .email(userDto.email())
                    .username(userDto.username())
                    .password(userDto.password().orElse(null))
                    .mobileNumber(userDto.mobileNumber())
                    .availabilityDate(userDto.availabilityDate().orElse(new Date()))
                    .role(role)
                    .build();
        }
        private User convertUserToEntity(UserDto userDto) {
            Role role  = roleRepository.findByName("expert");
            return User.builder()
                    .firstName(userDto.firstName())
                    .lastName(userDto.lastName())
                    .email(userDto.email())
                    .username(userDto.username())
                    .password(userDto.password().orElse(null))
                    .mobileNumber(userDto.mobileNumber())
                    .availabilityDate(userDto.availabilityDate().orElse(new Date()))
                    .role(role)
                    .country(userDto.country().orElse(null))
                    .build();
        }

        private UserDto mapUserEntityToDto(User user){
            return ImmutableUserDto.builder()
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .mobileNumber(user.getMobileNumber())
                    .build();

        }


    }

