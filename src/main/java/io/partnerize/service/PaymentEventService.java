package io.partnerize.service;

import io.partnerize.domain.PaymentEvent;
import io.partnerize.repository.PaymentEventRepository;
import io.partnerize.service.dto.PaymentEventDTO;
import io.partnerize.service.mapper.PaymentEventMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PaymentEvent}.
 */
@Service
@Transactional
public class PaymentEventService {

    private final Logger log = LoggerFactory.getLogger(PaymentEventService.class);

    private final PaymentEventRepository paymentEventRepository;

    private final PaymentEventMapper paymentEventMapper;

    public PaymentEventService(PaymentEventRepository paymentEventRepository, PaymentEventMapper paymentEventMapper) {
        this.paymentEventRepository = paymentEventRepository;
        this.paymentEventMapper = paymentEventMapper;
    }

    /**
     * Save a paymentEvent.
     *
     * @param paymentEventDTO the entity to save.
     * @return the persisted entity.
     */
    public PaymentEventDTO save(PaymentEventDTO paymentEventDTO) {
        log.debug("Request to save PaymentEvent : {}", paymentEventDTO);
        PaymentEvent paymentEvent = paymentEventMapper.toEntity(paymentEventDTO);
        paymentEvent = paymentEventRepository.save(paymentEvent);
        return paymentEventMapper.toDto(paymentEvent);
    }

    /**
     * Update a paymentEvent.
     *
     * @param paymentEventDTO the entity to save.
     * @return the persisted entity.
     */
    public PaymentEventDTO update(PaymentEventDTO paymentEventDTO) {
        log.debug("Request to update PaymentEvent : {}", paymentEventDTO);
        PaymentEvent paymentEvent = paymentEventMapper.toEntity(paymentEventDTO);
        paymentEvent = paymentEventRepository.save(paymentEvent);
        return paymentEventMapper.toDto(paymentEvent);
    }

    /**
     * Partially update a paymentEvent.
     *
     * @param paymentEventDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PaymentEventDTO> partialUpdate(PaymentEventDTO paymentEventDTO) {
        log.debug("Request to partially update PaymentEvent : {}", paymentEventDTO);

        return paymentEventRepository
            .findById(paymentEventDTO.getId())
            .map(existingPaymentEvent -> {
                paymentEventMapper.partialUpdate(existingPaymentEvent, paymentEventDTO);

                return existingPaymentEvent;
            })
            .map(paymentEventRepository::save)
            .map(paymentEventMapper::toDto);
    }

    /**
     * Get all the paymentEvents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PaymentEventDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PaymentEvents");
        return paymentEventRepository.findAll(pageable).map(paymentEventMapper::toDto);
    }

    /**
     * Get one paymentEvent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaymentEventDTO> findOne(Long id) {
        log.debug("Request to get PaymentEvent : {}", id);
        return paymentEventRepository.findById(id).map(paymentEventMapper::toDto);
    }

    /**
     * Delete the paymentEvent by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PaymentEvent : {}", id);
        paymentEventRepository.deleteById(id);
    }
}
