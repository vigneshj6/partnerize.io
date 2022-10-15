package io.partnerize.service;

import io.partnerize.domain.Partner;
import io.partnerize.repository.PartnerRepository;
import io.partnerize.service.dto.PartnerDTO;
import io.partnerize.service.mapper.PartnerMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Partner}.
 */
@Service
@Transactional
public class PartnerService {

    private final Logger log = LoggerFactory.getLogger(PartnerService.class);

    private final PartnerRepository partnerRepository;

    private final PartnerMapper partnerMapper;

    public PartnerService(PartnerRepository partnerRepository, PartnerMapper partnerMapper) {
        this.partnerRepository = partnerRepository;
        this.partnerMapper = partnerMapper;
    }

    /**
     * Save a partner.
     *
     * @param partnerDTO the entity to save.
     * @return the persisted entity.
     */
    public PartnerDTO save(PartnerDTO partnerDTO) {
        log.debug("Request to save Partner : {}", partnerDTO);
        Partner partner = partnerMapper.toEntity(partnerDTO);
        partner = partnerRepository.save(partner);
        return partnerMapper.toDto(partner);
    }

    /**
     * Update a partner.
     *
     * @param partnerDTO the entity to save.
     * @return the persisted entity.
     */
    public PartnerDTO update(PartnerDTO partnerDTO) {
        log.debug("Request to update Partner : {}", partnerDTO);
        Partner partner = partnerMapper.toEntity(partnerDTO);
        partner = partnerRepository.save(partner);
        return partnerMapper.toDto(partner);
    }

    /**
     * Partially update a partner.
     *
     * @param partnerDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PartnerDTO> partialUpdate(PartnerDTO partnerDTO) {
        log.debug("Request to partially update Partner : {}", partnerDTO);

        return partnerRepository
            .findById(partnerDTO.getId())
            .map(existingPartner -> {
                partnerMapper.partialUpdate(existingPartner, partnerDTO);

                return existingPartner;
            })
            .map(partnerRepository::save)
            .map(partnerMapper::toDto);
    }

    /**
     * Get all the partners.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PartnerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Partners");
        return partnerRepository.findAll(pageable).map(partnerMapper::toDto);
    }

    /**
     * Get one partner by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PartnerDTO> findOne(Long id) {
        log.debug("Request to get Partner : {}", id);
        return partnerRepository.findById(id).map(partnerMapper::toDto);
    }

    /**
     * Delete the partner by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Partner : {}", id);
        partnerRepository.deleteById(id);
    }
}
