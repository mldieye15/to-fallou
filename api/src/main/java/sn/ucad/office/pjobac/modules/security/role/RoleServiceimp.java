package sn.ucad.office.pjobac.modules.security.role;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ucad.office.pjobac.exception.BusinessResourceException;
import sn.ucad.office.pjobac.exception.ResourceAlreadyExists;
import sn.ucad.office.pjobac.modules.security.role.dto.RoleAudit;
import sn.ucad.office.pjobac.modules.security.role.dto.RoleRequest;
import sn.ucad.office.pjobac.modules.security.role.dto.RoleResponse;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class RoleServiceimp implements RoleService {
    private final RoleDao dao;
    private final RoleMapper mapper;

    @Override
    public List<RoleResponse> all() throws BusinessResourceException {
        log.info("Liste des roles. <all>");
        List<AppRole> roles = dao.findAll();
        List<RoleResponse> response = roles.stream()
                .map(role->mapper.toEntiteResponse(role))
                .collect(Collectors.toList());
        return response;
    }

    @Override
    public SimplePage<RoleResponse> all(Pageable pageable) throws BusinessResourceException {
        log.info("Liste des roles avec pagination. <all>");
        final Page<AppRole> page = dao.findAll(pageable);
        return new SimplePage<RoleResponse>(page.getContent()
                .stream()
                //.map(role -> mapToDTO(role, new RoleDTO()))
                .map(item->mapper.toEntiteResponse(item))
                .collect(Collectors.toList()),
                page.getTotalElements(), pageable);
    }

    @Override
    public Optional<RoleResponse> oneById(String id) throws NumberFormatException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            AppRole role = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Role avec "+id+" non trouvé(e).", HttpStatus.NOT_FOUND)
                    );
            log.info("Role avec id: "+id+" trouvé(e). <oneById>");
            Optional<RoleResponse> response = Optional.ofNullable(mapper.toEntiteResponse(role));
            return response;
        } catch (NumberFormatException e){
            log.warn("Paramétre id " + id + " non autorisé. <oneById>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre "+id+" non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public RoleResponse add(RoleRequest request) throws BusinessResourceException {
        try{
            log.info("Debug 001-request:  "+request.toString());
            //logger.info("Debug 002-username:  "+userService.user().getUsername());
            //AppRole role = mapper.anneRequestToAnneeAdd(request, userService.user());
            AppRole entity = mapper.requestToEntity(request);
            log.info("Debug 002-request_to_entity:  "+entity.toString());
            RoleResponse response = mapper.toEntiteResponse( dao.save(entity) );
            log.info("Ajout "+response.getNom()+" effectuée avec succés <add>");
            return response;
        } catch (ResourceAlreadyExists | DataIntegrityViolationException e) {
            log.error("Ajout role: donnée existante ou contrainte non respectée"+e.toString());
            throw new BusinessResourceException("data-error", "Donnée existante ou contrainte non respectée ", HttpStatus.CONFLICT);
        } catch(Exception ex){
            log.error("Ajout role: erreur inattandue est rencontrée."+ex.getMessage());
            throw new BusinessResourceException("technical-error", "Erreur technique de création d'une année: "+request.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public RoleResponse maj(RoleRequest request, String id) throws NumberFormatException, BusinessResourceException {
        try{
            Long myId = Long.valueOf(id.trim());
            AppRole entiteOptional = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Role avec "+id+" non trouvé(e).", HttpStatus.NOT_FOUND)
                    );
            AppRole entity = mapper.requestToEntiteUp(entiteOptional, request);
            RoleResponse response = mapper.toEntiteResponse( dao.save(entity) );
            log.info("Mise à jour "+response.getNom()+" effectué avec succés <maj>");
            return response;
        } catch (NumberFormatException e){
            log.warn("Paramétre id " + id + " non autorisé. <oneById>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre "+id+" non autorisé.", HttpStatus.BAD_REQUEST);
        } catch (ResourceAlreadyExists | DataIntegrityViolationException e) {
            log.error("Erreur technique de maj: donnée en doublon ou contrainte non respectée"+e.toString());
            throw new BusinessResourceException("data-error", "Donnée en doublon ou contrainte non respectée ", HttpStatus.CONFLICT);
        } catch(Exception ex){
            log.error("Maj role: Une erreur inattandue est rencontrée."+ex.toString());
            throw new BusinessResourceException("technical-error", "Erreur technique de maj role: "+request.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public String del(String id) throws NumberFormatException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            AppRole entity = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Role avec "+id+" non trouvé(e).", HttpStatus.NOT_FOUND)
                    );
            dao.deleteById(myId);
            log.info("Role avec id & nom: "+id+" & "+entity.getNom()+" supprimé avec succés. <del>");
            String response = "Role: "+entity.getNom()+" supprimée avec succés. <del>";
            return response;
        } catch (NumberFormatException e){
            log.warn("Paramétre id " + id + " non autorisé. <oneById>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre "+id+" non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Optional<RoleAudit> auditOneById(String id) throws NumberFormatException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            AppRole role = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Role avec "+id+" non trouvé(e).", HttpStatus.NOT_FOUND)
                    );
            log.info("Role avec id: "+id+" trouvé. <auditOneById>");
            //Optional<RoleAudit> response = Optional.ofNullable(mapper.toEntiteAudit(role, role.getUtiCree().getUsername(), annee.getUtiModifie().getUsername()));
            Optional<RoleAudit> response = Optional.ofNullable(mapper.toEntiteAudit(role, "admin", "admin"));
            return response;
        } catch (NumberFormatException e){
            log.warn("Paramétre id " + id + " non autorisé. <oneById>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre "+id+" non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Optional<AppRole> roleByNom(String nom) throws BusinessResourceException {
        try {
            AppRole entity = dao.findByNom(nom);
            Optional<AppRole> response = Optional.ofNullable(entity);
            log.info("Role avec nom: "+nom+" trouvé. <roleByNom>");
            return response;
        } catch(Exception ex){
            log.error("Role by nom: Une erreur inattandue est rencontrée."+ex.toString());
            throw new BusinessResourceException("not-found", "Role avec nom: "+nom+" non trouvé(e).", HttpStatus.NOT_FOUND);
        }
    }
}
