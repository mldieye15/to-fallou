package sn.ucad.office.pjobac.modules.detailsCandidat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import sn.ucad.office.pjobac.exception.BusinessResourceException;
import sn.ucad.office.pjobac.modules.demande.Demande;
import sn.ucad.office.pjobac.modules.demande.DemandeDao;
import sn.ucad.office.pjobac.modules.demande.DemandeMapper;
import sn.ucad.office.pjobac.modules.demande.dto.DemandeDetailsCandidatResponse;
import sn.ucad.office.pjobac.modules.etatDemande.EtatDemandeService;
import sn.ucad.office.pjobac.modules.ville.Ville;
import sn.ucad.office.pjobac.modules.ville.VilleDao;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class OrdreArriveService {
    private final DetailsCandidatDao dao;
    private  final DemandeDao demandeDao;
    private final DemandeMapper demandeMapper;
    private final VilleDao villeDao;
    private final EtatDemandeService etatDemandeService;
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    public void updateOrderByVille() throws BusinessResourceException {
//        log.info("DemandeServiceImp::updateOrderByVille");
//        // Récupérer toutes les demandes avec les détails de chaque candidat
//        List<Demande> all = demandeDao.demandeBySession();
//        // Regrouper les demandes par ville
//        Map<Long, List<DemandeDetailsCandidatResponse>> groupedByVille = all.stream()
//                .collect(Collectors.groupingBy(demande -> demande.getVille().getId(),
//                        Collectors.mapping(demande -> demandeMapper.mapToResponse(demande,
//                                dao.detailsForUserAndSession(demande.getUser())), Collectors.toList())));
//        // Pour chaque ville, trier les candidats par note et mettre à jour l'ordre d'arrivée
//        groupedByVille.forEach((city, candidates) -> {
//            candidates.sort(Comparator.comparingInt(DemandeDetailsCandidatResponse::getNote).reversed());
//            // Mettre à jour l'ordre d'arrivée
//            int order = 1;
//            for (DemandeDetailsCandidatResponse candidate : candidates) {
//                Demande demande = demandeDao.findById(candidate.getDemandeId()).orElse(null);
//                if (demande != null) {
//                    demande.setOrdreArrivee(order++);
//                    demandeDao.save(demande);
//                }
//            }
//        });
//    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateRangByVille() throws BusinessResourceException {
        log.info("DemandeServiceImp::updateOrderByVille");
        // Récupérer toutes les demandes avec les détails de chaque candidat
        List<Demande> all = demandeDao.demandePending();
        // Regrouper les demandes par ville
        Map<Long, List<DemandeDetailsCandidatResponse>> groupedByVille = all.stream()
                .collect(Collectors.groupingBy(demande -> demande.getVille().getId(),
                        Collectors.mapping(demande -> demandeMapper.mapToResponse(demande,
                                dao.detailsForUserAndSession(demande.getUser())), Collectors.toList())));
        // Pour chaque ville, trier les candidats par note et mettre à jour l'ordre d'arrivée
        groupedByVille.forEach((city, candidates) -> {
            candidates.sort(Comparator.comparingInt(DemandeDetailsCandidatResponse::getNote).reversed());
            // Mettre à jour l'ordre d'arrivée
            int rang = 1;
            for (DemandeDetailsCandidatResponse candidate : candidates) {
                Demande demande = demandeDao.findById(candidate.getDemandeId()).orElse(null);
                if (demande != null) {
                    demande.setRang(rang++);
                    demandeDao.save(demande);
                }
            }
        });

    }
//    public List<DetailsCandidat> findByVille(String villeId) {
//        try {
//            Long myId = Long.valueOf(villeId.trim());
//            Ville ville = villeDao.findById(myId)
//                    .orElseThrow(
//                            () -> new BusinessResourceException("not-found", "Aucune Ville avec " + villeId + " trouvé.", HttpStatus.NOT_FOUND)
//                    );
//            log.info("Ville avec id: " + villeId + " trouvé. <auditOneById>");
//            return dao.findByVille(ville);
//            //
//        } catch (NumberFormatException e) {
//            log.warn("Paramètre id " + villeId + " non autorisé. <auditOneById>.");
//            throw new BusinessResourceException("not-valid-param", "Paramètre " + villeId + " non autorisé.", HttpStatus.BAD_REQUEST);
//        }
//    }

}
