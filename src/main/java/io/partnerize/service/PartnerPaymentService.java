package io.partnerize.service;

import io.partnerize.domain.PartnerPayment;
import io.partnerize.repository.PartnerPaymentRepository;
import io.partnerize.service.dto.PartnerPaymentDTO;
import io.partnerize.service.mapper.PartnerPaymentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PartnerPayment}.
 */
@Service
@Transactional
public class PartnerPaymentService {

    private final Logger log = LoggerFactory.getLogger(PartnerPaymentService.class);

    private final PartnerPaymentRepository partnerPaymentRepository;

    private final PartnerPaymentMapper partnerPaymentMapper;

    public PartnerPaymentService(PartnerPaymentRepository partnerPaymentRepository, PartnerPaymentMapper partnerPaymentMapper) {
        this.partnerPaymentRepository = partnerPaymentRepository;
        this.partnerPaymentMapper = partnerPaymentMapper;
    }

    /**
     * Save a partnerPayment.
     *
     * @param partnerPaymentDTO the entity to save.
     * @return the persisted entity.
     */
    public PartnerPaymentDTO save(PartnerPaymentDTO partnerPaymentDTO) {
        log.debug("Request to save PartnerPayment : {}", partnerPaymentDTO);
        PartnerPayment partnerPayment = partnerPaymentMapper.toEntity(partnerPaymentDTO);
        partnerPayment = partnerPaymentRepository.save(partnerPayment);
        return partnerPaymentMapper.toDto(partnerPayment);
    }

    /**
     * Update a partnerPayment.
     *
     * @param partnerPaymentDTO the entity to save.
     * @return the persisted entity.
     */
    public PartnerPaymentDTO update(PartnerPaymentDTO partnerPaymentDTO) {
        log.debug("Request to update PartnerPayment : {}", partnerPaymentDTO);
        PartnerPayment partnerPayment = partnerPaymentMapper.toEntity(partnerPaymentDTO);
        partnerPayment = partnerPaymentRepository.save(partnerPayment);
        return partnerPaymentMapper.toDto(partnerPayment);
    }

    /**
     * Partially update a partnerPayment.
     *
     * @param partnerPaymentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PartnerPaymentDTO> partialUpdate(PartnerPaymentDTO partnerPaymentDTO) {
        log.debug("Request to partially update PartnerPayment : {}", partnerPaymentDTO);

        return partnerPaymentRepository
            .findById(partnerPaymentDTO.getId())
            .map(existingPartnerPayment -> {
                partnerPaymentMapper.partialUpdate(existingPartnerPayment, partnerPaymentDTO);

                return existingPartnerPayment;
            })
            .map(partnerPaymentRepository::save)
            .map(partnerPaymentMapper::toDto);
    }

    /**
     * Get all the partnerPayments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PartnerPaymentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PartnerPayments");
        return partnerPaymentRepository.findAll(pageable).map(partnerPaymentMapper::toDto);
    }

    /**
     * Get one partnerPayment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PartnerPaymentDTO> findOne(Long id) {
        log.debug("Request to get PartnerPayment : {}", id);
        return partnerPaymentRepository.findById(id).map(partnerPaymentMapper::toDto);
    }

    /**
     * Delete the partnerPayment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PartnerPayment : {}", id);
        partnerPaymentRepository.deleteById(id);
    }
}
