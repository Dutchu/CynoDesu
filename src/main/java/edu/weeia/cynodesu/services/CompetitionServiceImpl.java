package edu.weeia.cynodesu.services;

import edu.weeia.cynodesu.api.v1.mapper.DogMapper;
import edu.weeia.cynodesu.api.v1.mapper.DogMapperImpl;
import edu.weeia.cynodesu.api.v1.model.CompetitionScoreForm;
import edu.weeia.cynodesu.domain.*;
import edu.weeia.cynodesu.api.v1.mapper.CompetitionMapper;
import edu.weeia.cynodesu.api.v1.model.CompetitionCreateForm;
import edu.weeia.cynodesu.api.v1.model.CompetitionPreviewDto;
import edu.weeia.cynodesu.api.v1.model.CompetitionPreviewProjection;
import edu.weeia.cynodesu.exceptions.ResourceNotFoundException;
import edu.weeia.cynodesu.repositories.CompetitionRepository;
import edu.weeia.cynodesu.repositories.DogRepository;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CompetitionServiceImpl implements CompetitionService {

    private final CompetitionRepository competitionRepository;

    private final BreedingFacilityImpl breedingFacilityImpl;
    private final DogService dogService;
    private final PdfService pdfService;

    public CompetitionServiceImpl(CompetitionRepository competitionRepository, BreedingFacilityImpl breedingFacilityImpl, @Lazy DogService dogService, PdfService pdfService) {
        this.competitionRepository = competitionRepository;
        this.breedingFacilityImpl = breedingFacilityImpl;
        this.dogService = dogService;
        this.pdfService = pdfService;
    }

    @Override
    @Transactional
    public Long registerDogForCompetition(Long competitionId, Long dogId) {
        var foundCompetition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new ResourceNotFoundException("Competition with id: " + competitionId + " was not found"));
        var foundDog = dogService.findDogById(dogId);

        foundCompetition.addDog(foundDog);

        return foundCompetition.getId(); // Method 2
    }

    @Override
    @Transactional(readOnly = true)
    public CompetitionScoreForm getCompetitionScoreFormInformation(Long competitionId, Long dogId) {

        var foundCompetition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new ResourceNotFoundException("Competition with id: " + competitionId + " not found!"));
        var foundDog = dogService.findDogById(dogId);

        var formInfo = new CompetitionScoreForm();
        formInfo.setIcon(DogMapper.stringFromBytes(foundDog.getIcon()));
        formInfo.setDogId(foundDog.getId());
        formInfo.setDogName(foundDog.getName());
        formInfo.setUserId(foundDog.getCreatedByUser().getId());
        formInfo.setUserName(foundDog.getCreatedByUser().getUsername());
        formInfo.setCreatedAt(DogMapper.mapInstantToLocalDateTime(foundDog.getCreatedDate()));
        formInfo.setBreedingFacilityId(foundDog.getBreedingFacility().getId());
        formInfo.setBreedingFacilityName(foundDog.getBreedingFacility().getName());
        formInfo.setContent(foundDog.getContent());
        formInfo.setDocs(foundDog.getAttachedFiles());
        formInfo.setScores(new HashSet<>());

        return  formInfo;
    }

    @Override
    public Page<CompetitionPreviewDto> getUsersCompetitions(String username, Pageable pageable) {
        List<BreedingFacility> bfList = breedingFacilityImpl.findByUserUsername(username);

        Set<Long> ids = bfList.stream().map(BreedingFacility::getUserIds).flatMap(Set::stream).collect(Collectors.toSet());
        System.out.println("\nFOUND IDS: " + ids + " FOR USER: " + username + "\n");

        return competitionRepository.findAllByBreedingFacilityIds(ids, pageable);
    }

    @Override
    public Page<CompetitionPreviewDto> getAllCompetitions(Pageable pageable) {
        return competitionRepository.findAllPreviewPages(pageable).map(CompetitionMapper.INSTANCE::mapToPreview);
    }

    @Override
    @Transactional
    public Set<DogCompetitionScoreId> scoreDog(Long competitionId, Long dogId, CompetitionScoreForm scoreForm) {
        DogCompetitionScoreId result;
        var foundCompetition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new ResourceNotFoundException("Competition with id: " + competitionId + " not found!"));
        var foundDog = dogService.findDogById(dogId);

        Set<DogCompetitionScore> scores = foundCompetition.getScores().stream().filter(c -> c.getDog().getId().equals(foundDog.getId())).collect(Collectors.toSet());
        scores.forEach(s -> {
            s.setScore(scoreForm.getNewScore());
            s.setRank(scoreForm.getNewRank());
        });

        /***
         * if score was higher than 5 and rank higher than 3 Use fileService to generate document based on
         * scoreForm and foundCompetition
         * save Dog with a File to DB. Not sure if File should be saved first and later attached to a Dog,
         * or saving Dog with new file will persist new file
         */
        // Conditional logic for generating a document based on score and rank
        if (scoreForm.getNewScore() > 5 && scoreForm.getNewRank() > 3) {

            var number = UUID.randomUUID();
            var registrationNo = number.toString();

            // Generate the certificate/document
            ReceivedFile cert;
            try {
                cert = FileService.createReceivedFile(
                        "cert:" + registrationNo,
                        ReceivedFile.FileGroup.CERTIFICATE,
                        pdfService.generateDogCertificate(scoreForm, foundCompetition, registrationNo));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // Attach the generated document to the Dog entity
            foundDog.getAttachedFiles().add(cert);
            foundDog.setRegistrationNo(registrationNo);
        }

        return scores.stream().map(DogCompetitionScore::getId).collect(Collectors.toSet());
    }

    @Override
    public Long save(CompetitionCreateForm form) {
        return competitionRepository.save(CompetitionMapper.INSTANCE.mapToEntity(form)).getId();
    }

    public Competition findCompetition(Long id) {
        return competitionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Competition with id: " + id + " not found!"));
    }
}
