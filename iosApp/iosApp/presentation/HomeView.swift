

import SwiftUI
import Shared

struct HomeView: View {
    @StateObject private var viewModel = HomeViewModel(
           gameRepository: GameRepository()
       )

    
    var body: some View {
        NavigationView {
            ZStack {
                switch viewModel.state {
                case .idle:
                    Color.clear
                        .onAppear {
                            Task {
                                await viewModel.getGames()
                            }
                        }
                    
                case .loading:
                    LoadingView()
                    
                case .loaded(let games):
                    GameListView(games: games) {
                        Task {
                            await viewModel.getGames()
                        }
                    }
                    
                case .failure(let error):
                    ErrorView(message: error) {
                        Task {
                            await viewModel.getGames()
                        }
                    }
                }
            }
            .navigationTitle("Free Games")
            .navigationBarTitleDisplayMode(.large)
        }
    }
}


struct LoadingView: View {
    var body: some View {
        VStack(spacing: 16) {
            ProgressView()
                .scaleEffect(1.5)
            
            Text("Loading games...")
                .font(.subheadline)
                .foregroundColor(.secondary)
        }
    }
}


struct GameListView: View {
    let games: [Game]
    let onRefresh: () -> Void
    
    var body: some View {
        ScrollView {
            LazyVStack(spacing: 12) {
                ForEach(games, id: \.id) { game in
                    GameCardView(game: game)
                        .padding(.horizontal)
                }
            }
            .padding(.vertical)
        }
        .refreshable {
            onRefresh()
        }
    }
}


struct GameCardView: View {
    let game: Game
    
    var body: some View {
        HStack(alignment: .top, spacing: 12) {
            // Game Thumbnail
            AsyncImage(url: URL(string: game.thumbnail)) { phase in
                switch phase {
                case .empty:
                    ProgressView()
                        .frame(width: 100, height: 100)
                case .success(let image):
                    image
                        .resizable()
                        .aspectRatio(contentMode: .fill)
                        .frame(width: 100, height: 100)
                        .clipped()
                case .failure:
                    Image(systemName: "photo")
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: 100, height: 100)
                        .foregroundColor(.gray)
                @unknown default:
                    EmptyView()
                }
            }
            .cornerRadius(12)
            
            // Game Info
            VStack(alignment: .leading, spacing: 6) {
                Text(game.title)
                    .font(.headline)
                    .lineLimit(2)
                
                Text(game.shortDescription)
                    .font(.caption)
                    .foregroundColor(.secondary)
                    .lineLimit(3)
                
                Spacer()
                
                // Tags
                HStack(spacing: 8) {
                    TagView(text: game.developer, color: .blue)

                    TagView(text: game.developer, color: .blue)

                }
            }
            .frame(maxWidth: .infinity, alignment: .leading)
        }
        .padding()
        .background(Color(.systemBackground))
        .cornerRadius(16)
        .shadow(color: .black.opacity(0.1), radius: 5, x: 0, y: 2)
    }
}

struct TagView: View {
    let text: String
    let color: Color
    
    var body: some View {
        Text(text)
            .font(.caption2)
            .fontWeight(.medium)
            .padding(.horizontal, 10)
            .padding(.vertical, 5)
            .background(color.opacity(0.15))
            .foregroundColor(color)
            .cornerRadius(8)
    }
}

struct ErrorView: View {
    let message: String
    let onRetry: () -> Void
    
    var body: some View {
        VStack(spacing: 20) {
            Image(systemName: "exclamationmark.triangle.fill")
                .font(.system(size: 60))
                .foregroundColor(.red)
            
            Text("Oops!")
                .font(.title)
                .fontWeight(.bold)
            
            Text(message)
                .font(.body)
                .foregroundColor(.secondary)
                .multilineTextAlignment(.center)
                .padding(.horizontal, 40)
            
            Button(action: onRetry) {
                HStack {
                    Image(systemName: "arrow.clockwise")
                    Text("Try Again")
                }
                .font(.headline)
                .foregroundColor(.white)
                .padding()
                .background(Color.blue)
                .cornerRadius(12)
            }
        }
        .padding()
    }
}
