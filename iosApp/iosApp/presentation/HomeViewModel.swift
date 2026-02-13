//
//  HomeViewModel.swift
//  iosApp
//
//  Created by Omara on 13/02/2026.
//

import SwiftUI
import Shared

@MainActor
final class HomeViewModel: ObservableObject {
    private let gameRepository: GameRepository
    @Published private(set) var state: ViewState<[Game]> = .idle

    init(gameRepository: GameRepository) {
        self.gameRepository = gameRepository
    }
    
    func getGames() async {
        state = .loading
        
        do {
            let games : [Game] = try await gameRepository.getGames()
            state = .loaded(games)
        }catch {
            state = .failure(error.localizedDescription)
        }
    }
   
}
