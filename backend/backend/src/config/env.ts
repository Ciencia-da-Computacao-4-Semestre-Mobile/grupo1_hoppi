import { z } from 'zod'
import dotenv from 'dotenv'

dotenv.config()

const envSchema = z.object({
    DB_SERVICE_ROLE_KEY: z.jwt(),
    DB_HOST: z.string(),
    DB_PORT: z.coerce.number(),
    DB_USERNAME: z.string(),
    DB_PASSWORD: z.string(),
    SERVER_PORT: z.coerce.number()
})

const _env = envSchema.safeParse(process.env)
process.env.NODE_TLS_REJECT_UNAUTHORIZED = '0'

if(!_env.success){
    console.error("Erro na validação do env!")
    for (const issue of _env.error.issues) {
        console.error(`- [${issue.path.join(".")}] ${issue.message}`);
    }
    process.exit(1)
}

export const env = _env.data