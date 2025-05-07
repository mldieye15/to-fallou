package sn.ucad.office.pjobac.modules.canditatAuth;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sn.ucad.office.pjobac.exception.BusinessResourceException;
import sn.ucad.office.pjobac.exception.ResourceAlreadyExists;

import sn.ucad.office.pjobac.modules.canditatAuth.dto.CandidatAuthoriserAudit;
import sn.ucad.office.pjobac.modules.canditatAuth.dto.CandidatAuthoriserRequest;
import sn.ucad.office.pjobac.modules.canditatAuth.dto.CandidatAuthoriserResponse;
import sn.ucad.office.pjobac.modules.canditatAuth.dto.CandidatCSV;
import sn.ucad.office.pjobac.modules.demande.DemandeDao;
import sn.ucad.office.pjobac.modules.detailsCandidat.DetailsCandidat;
import sn.ucad.office.pjobac.modules.security.token.AuthService;
import sn.ucad.office.pjobac.modules.security.user.AppUser;
import sn.ucad.office.pjobac.modules.security.user.UserDao;
import sn.ucad.office.pjobac.modules.security.user.UserMapper;
import sn.ucad.office.pjobac.modules.security.user.dto.UserResponse;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CandidatAuthoriserServiceImp implements CandidatAuthoriserService {
    private final CandidatAuthoriserMapper mapper;
    private final CandidatAuthoriserDao dao;
    private final UserDao userDao;
    private final UserMapper userMapper;
    private final AuthService authService;


    @Override
    public List<CandidatAuthoriserResponse> all() throws BusinessResourceException {
        log.info("CandidatAuthoriserServiceImp::all");
        List<CandidatAuthoriser> all = dao.findAll();
        List<CandidatAuthoriserResponse> response;
        response = all.stream()
                .map(mapper::toEntiteResponse)
                .sorted(Comparator.comparing(CandidatAuthoriserResponse::getCode))
                .collect(Collectors.toList());
        return response;
    }

    @Override
    public List<UserResponse> allNotAuthoriser() throws BusinessResourceException {
        log.info("CandidatAuthoriserServiceImp::all");
        List<AppUser> all = dao.nonAutoriser();
        List<UserResponse> response;
        response = all.stream()
                .map(userMapper::toEntiteResponse)
                .sorted(Comparator.comparing(UserResponse::getCode))
                .collect(Collectors.toList());
        return response;
    }

    @Override
    public SimplePage<CandidatAuthoriserResponse> all(Pageable pageable) throws BusinessResourceException {
        log.info("Liste des Annees avec pagination. <all>");
        final Page<CandidatAuthoriser> page = dao.findAll(pageable);
        return new SimplePage<>(page.getContent()
                .stream()
                .map(mapper::toEntiteResponse)
                .collect(Collectors.toList()),
                page.getTotalElements(), pageable
        );
    }

    @Override
    public Optional<CandidatAuthoriserResponse> oneById(String id) throws NumberFormatException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            CandidatAuthoriser one = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun CandidatAuthoriser avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            log.info("Agen avec id: " + id + " trouvé. <oneById>");
            Optional<CandidatAuthoriserResponse> response;
            response = Optional.ofNullable(mapper.toEntiteResponse(one));
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <oneById>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public CandidatAuthoriserResponse add(CandidatAuthoriserRequest req) throws BusinessResourceException {
        try {
            log.info("Debug 001-add:  " + req.toString());
            CandidatAuthoriser one = mapper.requestToEntity(req);
            log.info("Debug 001-req_to_entity:  " + one.toString());
            CandidatAuthoriserResponse response = mapper.toEntiteResponse(dao.save(one));
            log.info("Ajout " + response.getCode() + " effectué avec succés. <add>");
            return response;
        } catch (ResourceAlreadyExists | DataIntegrityViolationException e) {
            log.error("Erreur technique de creation CandidatAuthoriser: donnée en doublon ou contrainte non respectée" + e.toString());
            throw new BusinessResourceException("data-error", "Donnée en doublon ou contrainte non respectée ", HttpStatus.CONFLICT);
        } catch (Exception ex) {
            log.error("Ajout CandidatAuthoriser: Une erreur inattandue est rencontrée." + ex.getMessage());
            throw new BusinessResourceException("technical-error", "Erreur technique de création d'un CandidatAuthoriser: " + req.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public CandidatAuthoriserResponse maj(CandidatAuthoriserRequest req, String id) throws NumberFormatException, NoSuchElementException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            CandidatAuthoriser CandidatAuthoriserOptional = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun CandidatAuthoriser avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            //CandidatAuthoriser annee = mapper.anneRequestToAnneeUp(CandidatAuthoriserOptional, req, userService.user());
            CandidatAuthoriser oneBrute = mapper.requestToEntiteUp(CandidatAuthoriserOptional, req);
            CandidatAuthoriserResponse response = mapper.toEntiteResponse(dao.save(oneBrute));
            log.info("Mise à jour " + response.getCode() + " effectuée avec succés. <maj>");
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <maj>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        } catch (ResourceAlreadyExists | DataIntegrityViolationException e) {
            log.error("Erreur technique de maj CandidatAuthoriser: donnée en doublon ou contrainte non respectée" + e.toString());
            throw new BusinessResourceException("data-error", "Donnée en doublon ou contrainte non respectée ", HttpStatus.CONFLICT);
        } catch (Exception ex) {
            log.error("Maj imputation: Une erreur inattandue est rencontrée." + ex.toString());
            throw new BusinessResourceException("technical-error", "Erreur technique de mise à jour d'un CandidatAuthoriser: " + req.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public String del(String id) throws NumberFormatException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            CandidatAuthoriser oneBrute = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun CandidatAuthoriser avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            dao.deleteById(myId);
            log.info("CandidatAuthoriser avec id & matricule: " + id + " & " + oneBrute.getMatricule() + " supprimé avec succés. <del>");
            String response;
            response = "Imputation: " + oneBrute.getMatricule() + " supprimé avec succés. <del>";
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <del>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Optional<CandidatAuthoriserAudit> auditOneById(String id) throws NumberFormatException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            CandidatAuthoriser oneBrute = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucune CandidatAuthoriser avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            log.info("CandidatAuthoriser avec id: " + id + " trouvé. <auditOneById>");
           Optional<CandidatAuthoriserAudit> response;
            response = Optional.ofNullable(mapper.toEntiteAudit(oneBrute, Long.valueOf("1"), Long.valueOf("1") ));
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <auditOneById>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        }
        //return Optional.empty();
    }

    @Override
    public Map<String, Object> importerCandidat(MultipartFile file) throws Exception {
        Map<String, Object> response = new HashMap<>();
        List<String> erreurs = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            CsvToBean<CandidatCSV> csvToBean = new CsvToBeanBuilder<CandidatCSV>(reader)
                    .withType(CandidatCSV.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<CandidatCSV> csvCandidats = csvToBean.parse();

            // Récupérer les codes et matricules de la base
            Set<String> existingCodes = new HashSet<>(dao.findAllCodes());
//            Set<String> existingMatricules = new HashSet<>(dao.findAllMatricules());

            // Filtrer les candidats valides et collecter les erreurs
            List<CandidatAuthoriser> candidatsValides = new ArrayList<>();
            for (CandidatCSV csv : csvCandidats) {
                if (existingCodes.contains(csv.getCode())) {
                    erreurs.add("Doublon de code : " + csv.getCode());
                    continue;
                }
//                if (existingMatricules.contains(csv.getMatricule())) {
//                    erreurs.add("Doublon de matricule : " + csv.getMatricule());
//                    continue;
//                }

                // Ajouter aux enregistrements valides
                candidatsValides.add(CandidatAuthoriser.builder()
                        .code(csv.getCode())
                        .matricule(csv.getMatricule())
                        .prenoms(csv.getPrenoms())
                        .nom(csv.getNom())
                        .telephone(csv.getTelephone())
                        .etablisement(csv.getEtablisement())
                        .build());

                // Ajouter aux ensembles pour éviter les doublons dans le fichier lui-même
                existingCodes.add(csv.getCode());
//                existingMatricules.add(csv.getMatricule());
            }

            // Sauvegarde des candidats valides
            if (!candidatsValides.isEmpty()) {
                dao.saveAll(candidatsValides);
            }

            response.put("message", "Importation terminée");
            response.put("candidats_insérés", candidatsValides.size());
            response.put("erreurs", erreurs);

            return response;

        } catch (Exception e) {
            e.printStackTrace(); // Affiche l'erreur complète dans la console
            throw new RuntimeException("Erreur lors de l'importation du fichier CSV : " + e.getMessage(), e);
        }
    }

    @Override
    public void autorisation(Long id) {
        AppUser user = userDao.findById(id).orElse(null);
        AppUser currentUser = authService.getCurrentUser();
        LocalDateTime date= LocalDateTime.now(ZoneOffset.UTC);

        if (user != null) {
            try {
//                user.setAuteur(currentUser);
                user.setDateModification(date);
                user.setEnabled(!user.isEnabled());
                userDao.save(user);
                log.info("Autorisationavec l'ID " + id + " changée avec succès.");
            } catch (Exception e) {
                log.error("Erreur lors de la sauvegarde de l'autorisation pour l'ID " + id, e);
            }
        } else {
            log.warn("Autorisation avec l'ID " + id + " non trouvé.");
        }

    }

    @Override
    public boolean hasEnabledNonAutoriserUsers() {
        log.info("hasEnabledNonAutoriserUsers");
        return dao.existsEnabledNonAutoriser();

    }
    @Transactional
    @Override
    public int disableAllNonAutoriserUsers() {
        log.info("Comptes désactivés avec success");
        return dao.disableNonAutoriserUsers();
    }


}

