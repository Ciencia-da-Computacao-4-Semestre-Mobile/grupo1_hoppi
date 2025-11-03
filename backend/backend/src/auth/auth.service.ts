import { Injectable } from "@nestjs/common";
import { InjectRepository } from "@nestjs/typeorm";
import { User } from "src/users/users.entity";
import { Repository } from "typeorm";

@Injectable()
export class AuthService {

    constructor(
        @InjectRepository(User) private readonly userRepository: Repository<User>,
    ) {}

  async login() {
    const user = await this.userRepository.findOneBy({ id: "378129hdsadhu-829w" });

    return this.userRepository.find();
  }

  async register() {
    return "Registration successful";
  }

  async forgot() {
    return "Password reset link sent";
  }
}