package io.partnerize.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link io.partnerize.domain.PartnerRevene} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PartnerReveneDTO implements Serializable {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PartnerReveneDTO)) {
            return false;
        }

        PartnerReveneDTO partnerReveneDTO = (PartnerReveneDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, partnerReveneDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PartnerReveneDTO{" +
            "id=" + getId() +
            "}";
    }
}
