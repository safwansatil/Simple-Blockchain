A blockchain is just a chain/list of blocks. Each block in the blockchain will have its own digital fingerprint, contain digital fingerprint of the previous block, and have some data ( this data could be transactions for example ).

### Hash = digital footprint

Sha-256 : takes a input string and converts to a fixed sixze unique fingerprint (hash), returns hex string, is case sensitive


StringBuffer : used when building string in loops, since it is mutable. and avoids ccreeating temporary objects


publicKey acts as address, while privateKey is useed to sign our transactions
The private key is used to sign the data and the public key can be used to verify its integrity

##### Elliptic curve keypair
a randomly generated private key (d) and a public key Q where Q=dxG, G is a fixed point on the elliptic curve
Recovering d from Q is computationally infeasible due to the Elliptic Curve Discrete Logarithm Problem (ECDLP).

