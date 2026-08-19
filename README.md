# Backend local

## Services requis

Depuis ce dossier, lance PostgreSQL, MinIO et MailHog :

```bash
docker compose up -d
```

- MinIO API : `http://localhost:9000`
- Console MinIO : `http://localhost:9001`
- MailHog : `http://localhost:8025`

Les identifiants et paramètres de développement sont dans `.env.example`. Copiez-le vers `.env` uniquement si vous devez les modifier. Ne versionnez jamais les secrets de production.

## Médias

Les fichiers de posts sont déposés dans le bucket MinIO `galsensport-media`. Le conteneur d'initialisation crée ce bucket et le rend lisible pour le développement local. En production, les objets devront être privés avec des URL signées.
# GalsenSport backend

## Comptes administrateur locaux

Avec `SEED_ADMINS_ENABLED=true`, le démarrage crée seulement si nécessaire :

- `admin@galsensport.local`
- `moderation@galsensport.local`

Les mots de passe viennent de `SEED_ADMIN_PRIMARY_PASSWORD` et
`SEED_ADMIN_SECONDARY_PASSWORD`. Changez-les avant tout déploiement ; désactivez
le seeding en production avec `SEED_ADMINS_ENABLED=false`.
