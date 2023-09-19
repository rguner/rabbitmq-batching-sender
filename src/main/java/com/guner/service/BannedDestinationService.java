package com.guner.service;

import com.guner.entity.BannedDestination;
import com.guner.exception.ResourceNotFoundException;
import com.guner.repository.BannedDestinationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class BannedDestinationService {
    private final BannedDestinationRepository bannedDestinationRepository;

    public List<BannedDestination> getAllBannedDestinations() {
        List<BannedDestination> bannedDestinationList = (List<BannedDestination>) bannedDestinationRepository.findAll();
        log.debug("getAllbannedDestinations: {} bannedDestinations retrieved from database", bannedDestinationList.size());
        return bannedDestinationList;
    }

    public BannedDestination getBannedDestinationsById(long id) {
        BannedDestination bannedDestination = bannedDestinationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found bannedDestination with id = " + id));

        log.debug("getbannedDestinationsById: bannedDestination retrieved from database, GsmNo: {}", bannedDestination.getGsmNo());
        return bannedDestination;
    }

    public BannedDestination createBannedDestination(BannedDestination bannedDestination) {
        return createBannedDestination(bannedDestination, true);
    }

    public BannedDestination createBannedDestination(BannedDestination bannedDestination, boolean sendEvent) {
        BannedDestination bannedDestination1 = bannedDestinationRepository.save(
                BannedDestination.builder()
                        .gsmNo(bannedDestination.getGsmNo())
                        .updatedDate(LocalDateTime.now())
                        .build());
        log.debug("ADD bannedDestination {}", bannedDestination.getGsmNo());
        return bannedDestination1;
    }

    public BannedDestination updateBannedDestination(long id, BannedDestination bannedDestination) {
        BannedDestination bannedDestination1 = bannedDestinationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found bannedDestination with id = " + id));

        bannedDestination1.setGsmNo(bannedDestination.getGsmNo());
        bannedDestination1.setUpdatedDate(LocalDateTime.now());

        BannedDestination bannedDestinationSaved = bannedDestinationRepository.save(bannedDestination1);
        log.debug("UPDATE bannedDestination {} update to {}", bannedDestination1.getGsmNo(), bannedDestination1.getGsmNo());
        return bannedDestinationSaved;
    }

    public void deleteBannedDestination(long id) {
        Optional<BannedDestination> optionalbannedDestination = bannedDestinationRepository.findById(id);
        if (optionalbannedDestination.isPresent()) {
            bannedDestinationRepository.deleteById(id);
            log.debug("DELETE bannedDestination {}", optionalbannedDestination.get().getGsmNo());
        } else {
            throw new ResourceNotFoundException("Not found bannedDestination with id = " + id);
        }
    }

    public BannedDestination updateBannedDestinationByGsmNo(String gsmNo, BannedDestination bannedDestination) {
        BannedDestination bannedDestination1 = bannedDestinationRepository.findByGsmNo(gsmNo).stream().findAny()
                .orElseThrow(() -> new ResourceNotFoundException("Not found bannedDestination with gsmNo = " + gsmNo));

        bannedDestination1.setGsmNo(bannedDestination.getGsmNo());
        bannedDestination1.setUpdatedDate(LocalDateTime.now());

        BannedDestination bannedDestinationSaved = bannedDestinationRepository.save(bannedDestination1);
        log.debug("UPDATE bannedDestination  {} update to {}", gsmNo, bannedDestination.getGsmNo());
        return bannedDestinationSaved;
    }

    @Transactional
    public void deleteBannedDestinationByGsmMo(String gsmNo) {
        if (bannedDestinationRepository.existsByGsmNo(gsmNo)) {
            bannedDestinationRepository.deleteByGsmNo(gsmNo);
            log.debug("DELETE bannedDestination {}", gsmNo);
        } else {
            throw new ResourceNotFoundException("Not found bannedDestination with gsmNo = " + gsmNo);
        }
    }

    public void insertTestData() {
        IntStream.rangeClosed(1, 1000).forEach(i -> {
            createBannedDestination(BannedDestination.builder()
                            .gsmNo(String.valueOf(5551000000L + i))
                            .updatedDate(LocalDateTime.now())
                            .build()
                    , false);
        });
        log.debug("bannedDestinations inserted");
    }
}
