package io.partnerize.service;

import io.partnerize.domain.PartnerRevene;
import io.partnerize.repository.PartnerReveneRepository;
import io.partnerize.service.dto.PartnerReveneDTO;
import io.partnerize.service.mapper.PartnerReveneMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PartnerRevene}.
 */
@Service
@Transactional
public class PartnerReveneService {

    private final Logger log = LoggerFactory.getLogger(PartnerReveneService.class);

    private final PartnerReveneRepository partnerReveneRepository;

    private final PartnerReveneMapper partnerReveneMapper;

    public PartnerReveneService(PartnerReveneRepository partnerReveneRepository, PartnerReveneMapper partnerReveneMapper) {
        this.partnerReveneRepository = partnerReveneRepository;
        this.partnerReveneMapper = partnerReveneMapper;
    }

    /**
     * Save a partnerRevene.
     *
     * @param partnerReveneDTO the entity to save.
     * @return the persisted entity.
     */
    public PartnerReveneDTO save(PartnerReveneDTO partnerReveneDTO) {
        log.debug("Request to save PartnerRevene : {}", partnerReveneDTO);
        PartnerRevene partnerRevene = partnerReveneMapper.toEntity(partnerReveneDTO);
        partnerRevene = partnerReveneRepository.save(partnerRevene);
        return partnerReveneMapper.toDto(partnerRevene);
    }

    /**
     * Update a partnerRevene.
     *
     * @param partnerReveneDTO the entity to save.
     * @return the persisted entity.
     */
    public PartnerReveneDTO update(PartnerReveneDTO partnerReveneDTO) {
        log.debug("Request to update PartnerRevene : {}", partnerReveneDTO);
        PartnerRevene partnerRevene = partnerReveneMapper.toEntity(partnerReveneDTO);
        // no save call needed as we have no fields that can be updated
        return partnerReveneMapper.toDto(partnerRevene);
    }

    /**
     * Partially update a partnerRevene.
     *
     * @param partnerReveneDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PartnerReveneDTO> partialUpdate(PartnerReveneDTO partnerReveneDTO) {
        log.debug("Request to partially update PartnerRevene : {}", partnerReveneDTO);

        return partnerReveneRepository
            .findById(partnerReveneDTO.getId())
            .map(existingPartnerRevene -> {
                partnerReveneMapper.partialUpdate(existingPartnerRevene, partnerReveneDTO);

                return existingPartnerRevene;
            })
            // .map(partnerReveneRepository::save)
            .map(partnerReveneMapper::toDto);
    }

    /**
     * Get all the partnerRevenes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PartnerReveneDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PartnerRevenes");
        return partnerReveneRepository.findAll(pageable).map(partnerReveneMapper::toDto);
    }

    /**
     * Get one partnerRevene by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PartnerReveneDTO> findOne(Long id) {
        log.debug("Request to get PartnerRevene : {}", id);
        return partnerReveneRepository.findById(id).map(partnerReveneMapper::toDto);
    }

    /**
     * Delete the partnerRevene by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PartnerRevene : {}", id);
        partnerReveneRepository.deleteById(id);
    }
}
