package com.teles.candidaturas.api.service;

import com.teles.candidaturas.api.domain.dto.CandidaturaRequest;
import com.teles.candidaturas.api.domain.dto.CandidaturaResponse;
import com.teles.candidaturas.api.domain.entity.Candidatura;
import com.teles.candidaturas.api.repository.CandidaturaRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CandidaturaService {

    private final CandidaturaRepository candidaturaRepository;

    private final ModelMapper modelMapper;

    public CandidaturaService(CandidaturaRepository candidaturaRepository, ModelMapper modelMapper) {
        this.candidaturaRepository = candidaturaRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public void save(CandidaturaRequest candidaturaRequest) {

        Candidatura candidatura = new Candidatura();
        candidatura.setPessoaId(candidaturaRequest.getPessoaId());
        candidatura.setVagaId(candidaturaRequest.getVagaId());

        candidatura.setScore(calculateScore(candidaturaRequest));

        candidaturaRepository.save(candidatura);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<CandidaturaResponse> findByVagaId(Long vagaId) {

        log.info("Searching for Candidaturas by vagaId: [{}]", vagaId);

        return candidaturaRepository.findByVagaIdOrderByScoreDesc(vagaId)
                .stream()
                .map(this::mapToCandidaturaResponse)
                .collect(Collectors.toList());
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public Optional<Candidatura> findByVagaIdAndPessoaId(Long vagaId, Long pessoaId) {

        log.info("Searching Candidatura by vagaId: [{}] and pessoaId: [{}]", vagaId, pessoaId);

        return candidaturaRepository.findByVagaIdAndPessoaId(vagaId, pessoaId);
    }

    private Integer calculateScore(CandidaturaRequest candidatura) {
        //TODO calcular score
        return new Random().nextInt(100);
    }

    private CandidaturaResponse mapToCandidaturaResponse(Candidatura candidatura) {

        return modelMapper.map(candidatura, CandidaturaResponse.class);
    }

}
