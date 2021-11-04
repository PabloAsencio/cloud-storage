package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {

    private CredentialMapper credentialMapper;
    private EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public int createCredential(Credential credential) {
        String key = generateKey();
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), key);
        return credentialMapper.insertCredential(new Credential(null, credential.getUrl(), credential.getUsername(), encryptedPassword, key, credential.getUserid()));
    }

    public List<Credential> getUserCredentials(Integer userid) {
        return credentialMapper.getUserCredentials(userid);
    }

    public Credential getCredentialById(Integer credentialid) {
        return credentialMapper.getCredentialById(credentialid);
    }

    public String getDecryptedPassword(Credential credential) {
        return encryptionService.decryptValue(credential.getPassword(), credential.getKey());
    }

    public void updateCredential(Credential credential) {
        String key = generateKey();
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), key);
        credential.setKey(key);
        credential.setPassword(encryptedPassword);
        credentialMapper.updateCredential(credential);
    }

    private static String generateKey() {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        return Base64.getEncoder().encodeToString(key);
    }

    public void deleteCredential(Integer credentialid) {
        credentialMapper.deleteCredential(credentialid);
    }
}
