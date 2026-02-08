# RSA Keys

This directory stores the RSA key pair used for JWT authentication.

Expected files:
- VUTTR_rsaPrivateKey.pem
- VUTTR_rsaPublicKey.pem

These files are intentionally not committed to the repository.

## How keys are generated

For local development and portfolio purposes, RSA keys are generated
automatically by a Docker Compose utility service (`keygen`).